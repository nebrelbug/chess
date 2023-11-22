package client;

import java.util.Arrays;

import com.google.gson.Gson;
import exceptions.ResponseException;
import models.AuthToken;
import server.ServerFacade;

import static ui.EscapeSequences.*;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.LOGGED_OUT;
    private AuthToken authToken = null;

    public ChessClient(String serverUrl) {
        server = new server.ServerFacade(serverUrl);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
//                case "list" -> listPets();
                case "logout" -> logout();
//                case "adopt" -> adoptPet(params);
//                case "adoptall" -> adoptAllPets();
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

    private String logout() {
        try {
            server.logout(authToken.authToken());
            state = State.LOGGED_OUT;

            return "Successfully signed out";

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
            {"create <NAME>", "a game"},
            {"list", "games"},
            {"join <ID> [WHITE|BLACK|<empty>]", "a game"},
            {"observe <ID>", "a game"},
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