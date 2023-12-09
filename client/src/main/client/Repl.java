package client;

import exceptions.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;
import websocket.NotificationHandler;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final ChessClient client;

    public Repl(String serverUrl) throws ResponseException {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome to GubChess! Type `help` to get started.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print(client.inputPrompt());
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }

    public void notify(ServerMessage notification) {
        System.out.println("I got called");
        System.out.println(SET_TEXT_COLOR_RED + notification.getServerMessageType());
    }

}