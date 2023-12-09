package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import exceptions.ResponseException;
import models.AuthToken;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, ResponseException, DataAccessException {
        UserGameCommand userCommand = new Gson().fromJson(message, UserGameCommand.class);

        var dao = new AuthDAO();

        var authToken = dao.getByTokenString(
                userCommand.getAuthString()
        );

        dao.close();

        int gameId = userCommand.getGameID();
        ChessMove move = userCommand.getMove();
        ChessGame.TeamColor color = userCommand.getColor();

        System.out.println("Received user game command");

        switch (userCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(authToken, session, color);
            case JOIN_OBSERVER -> joinObserver(authToken, session);
            case MAKE_MOVE -> makeMove(authToken, session);
            case LEAVE -> leave(authToken, session);
            case RESIGN -> resign(authToken, session);
        }
    }

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.err.println(throwable);
    }

    private void joinPlayer(AuthToken authToken, Session session, ChessGame.TeamColor color) throws IOException {
        connections.add(authToken.username(), session);

        connections.singleBroadcast(authToken.username(), new ServerMessage(
                ServerMessage.ServerMessageType.LOAD_GAME, null)
        );

        var notification = new ServerMessage(
                ServerMessage.ServerMessageType.NOTIFICATION,
                String.format("%s is joining as %s", authToken.username(), color.toString())
        );
        connections.broadcastExcluding(authToken.username(), notification);
    }

    private void joinObserver(AuthToken authToken, Session session) throws IOException {
        connections.add(authToken.username(), session);

        connections.singleBroadcast(authToken.username(), new ServerMessage(
                ServerMessage.ServerMessageType.LOAD_GAME, null)
        );

        var notification = new ServerMessage(
                ServerMessage.ServerMessageType.NOTIFICATION,
                String.format("%s started watching the game", authToken.username())
        );
        connections.broadcastExcluding(authToken.username(), notification);
    }

    private void makeMove(AuthToken authToken, Session session) throws IOException {
        connections.add(authToken.username(), session);
        var message = String.format("%s is in the shop", authToken);
//        var notification = new ServerMessage(ServerMessage.Type.ARRIVAL, message);
//        connections.broadcast(authToken.username(), notification);
    }


    private void leave(AuthToken authToken, Session session) throws IOException {
        connections.remove(authToken.username());

        var notification = new ServerMessage(
                ServerMessage.ServerMessageType.NOTIFICATION,
                String.format("%s left the game for now", authToken.username())
        );

        connections.broadcastExcluding("", notification);

    }


    private void resign(AuthToken authToken, Session session) throws IOException {
        connections.remove(authToken.username());

        var notification = new ServerMessage(
                ServerMessage.ServerMessageType.NOTIFICATION,
                String.format("%s left the game for now", authToken.username())
        );

        connections.broadcastExcluding("", notification);
    }

}