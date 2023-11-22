package client;

import java.util.Arrays;

import com.google.gson.Gson;
import exceptions.ResponseException;

import static ui.EscapeSequences.*;

public class ChessClient {
    private final String serverUrl;
    private State state = State.LOGGED_OUT;

    public ChessClient(String serverUrl) {
//        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
//                case "signin" -> signIn(params);
//                case "rescue" -> rescuePet(params);
//                case "list" -> listPets();
//                case "signout" -> signOut();
//                case "adopt" -> adoptPet(params);
//                case "adoptall" -> adoptAllPets();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    String[][] loggedOutCommands = {
            {"register <USERNAME> <PASSWORD> <EMAIL>", "to create an account"},
            {"login <USERNAME> <PASSWORD>", "to play chess"},
            {"quit", "playing chess"},
            {"help", "with possible commands"}
    };

    String[][] loggedInCommands = {
            {"create <NAME>", "a game"},
            {"list", "games"},
            {"join <ID> [WHITE|BLACK|<empty>]", "a game"},
            {"observe <ID>", "a game"},
            {"logout", "when you are done"},
            {"quit", "playing chess"},
            {"help", "with possible commands"}
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


    public String help() {
        if (state == State.LOGGED_OUT) {
            return generateHelpMenu(loggedOutCommands);
        }

        return generateHelpMenu(loggedInCommands);
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