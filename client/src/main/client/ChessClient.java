package client;

import java.util.Arrays;
import java.util.Objects;

import exceptions.ResponseException;
import models.AuthToken;
import server.ServerFacade;
import ui.BoardDisplay;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import static ui.EscapeSequences.*;

public class ChessClient {
    private final ServerFacade server;
    private final WebSocketFacade ws;
    private State state = State.LOGGED_OUT;
    private AuthToken authToken = null;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) throws ResponseException {
        server = new server.ServerFacade(serverUrl);
        ws = new WebSocketFacade(serverUrl, notificationHandler);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "create" -> create(params);
                case "list" -> list();
                case "logout" -> logout();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String login(String[] params) {
        try {
            authToken = server.login(params[0], params[1]);
            state = State.LOGGED_IN;

            return "Welcome to GubChess, " + authToken.username() + "!";

        } catch (ResponseException e) {
            return formatError("Invalid username or password");
        }
    }

    private String register(String[] params) {
        try {
            authToken = server.register(params[0], params[1], params[2]);
            state = State.LOGGED_IN;

            return "Welcome to GubChess, " + authToken.username() + "!";

        } catch (ResponseException e) {
            return formatError("Failed to register user");
        }
    }

    private String create(String[] params) throws ResponseException {

        assertSignedIn();

        try {
            int newGameId = server.create(authToken.authToken(), params[0]);

            return "Created new game with ID #" + newGameId;

        } catch (ResponseException e) {
            return formatError("Failed to register user");
        }
    }

    private String list() throws ResponseException {

        assertSignedIn();

        try {

            var sb = new StringBuilder();

            var games = server.list(authToken.authToken());

            for (var i = 0; i < games.size(); i++) {
                var game = games.get(i);

                if (i > 0) sb.append("\n");

                sb.append(game.gameName())
                        .append(" (#")
                        .append(game.gameID())
                        .append(")");

                String whiteUsername = game.whiteUsername() != null ? game.whiteUsername() : "nobody yet";
                String blackUsername = game.blackUsername() != null ? game.blackUsername() : "nobody yet";

                sb.append("\n   ")
                        .append("Playing white: ")
                        .append(whiteUsername);

                sb.append("\n   ")
                        .append("Playing black: ")
                        .append(blackUsername);

            }

            return sb.toString();

        } catch (ResponseException e) {
            return formatError("Failed to list games");
        }
    }

    private String join(String[] params) throws ResponseException {

        assertSignedIn();

        try {
            server.join(authToken.authToken(), Integer.parseInt(params[0]), params[1]);

            var game = server.getGame(authToken.authToken(), Integer.parseInt(params[0]));

            if (Objects.equals(params[1], "white")) {
                System.out.println(BoardDisplay.display(game, true));
                System.out.println(BoardDisplay.display(game, false));
            } else {
                System.out.println(BoardDisplay.display(game, false));
                System.out.println(BoardDisplay.display(game, true));
            }

            ws.joinPlayer(authToken.authToken(), params[1]);

            return "Successfully joined game #" + params[0] + " as color " + params[1];

        } catch (ResponseException e) {
            return formatError("Failed to join game: " + e.getMessage());
        }
    }

    private String observe(String[] params) throws ResponseException {
        assertSignedIn();

        try {
            server.join(authToken.authToken(), Integer.parseInt(params[0]), null);

            var game = server.getGame(authToken.authToken(), Integer.parseInt(params[0]));

            System.out.println(BoardDisplay.display(game, false));

            return "Successfully watching game #" + params[0];

        } catch (ResponseException e) {
            return formatError("Failed to observe game: " + e.getMessage());
        }
    }

    private String logout() throws ResponseException {

        assertSignedIn();

        try {
            server.logout(authToken.authToken());
            state = State.LOGGED_OUT;

            return "Successfully logged out";

        } catch (ResponseException e) {
            return formatError("Failed to log out");
        }
    }


    String[][] loggedOutCommands = {
            {"register <USERNAME> <PASSWORD> <EMAIL>", "to create an account"}, // implemented
            {"login <USERNAME> <PASSWORD>", "to play chess"}, // implemented
            {"quit", "playing chess"}, // implemented
            {"help", "with possible commands"} // implemented
    };

    String[][] loggedInCommands = {
            {"create <NAME>", "a game"}, // implement
            {"list", "games"}, // implemented
            {"join <ID> [WHITE|BLACK|<empty>]", "a game"}, // implemented // TODO show board
            {"observe <ID>", "a game"}, // implemented // TODO show board
            {"logout", "when you are done"}, // implemented
            {"quit", "playing chess"}, // implemented
            {"help", "with possible commands"} // implemented
    };

    private String generateHelpMenu(String[][] commands) {
        StringBuilder sb = new StringBuilder();

        for (var i = 0; i < commands.length; i++) {

            String[] command = commands[i];

            if (i > 0) sb.append("\n");
            sb.append("  ");
            sb.append(SET_TEXT_COLOR_BLUE);
            sb.append(command[0]);
            sb.append(SET_TEXT_COLOR_MAGENTA + " - ");
            sb.append(command[1]);
        }

        return sb.toString();
    }


    private String help() {
        if (state == State.LOGGED_OUT) {
            return generateHelpMenu(loggedOutCommands);
        }

        return generateHelpMenu(loggedInCommands);
    }

    private String formatError(String error) {
        return SET_TEXT_COLOR_RED + error;
    }

    public String inputPrompt() {
        return "\n" + SET_TEXT_COLOR_YELLOW + "[" + state + "] >>> " + SET_TEXT_COLOR_GREEN;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGED_OUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}