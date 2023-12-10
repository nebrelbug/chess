package websocket;

import chess.ChessGame;
import chess.ChessMove;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import exceptions.ResponseException;
import models.AuthToken;
import models.Deserializer;
import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Objects;

import response.Stringifier;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final AuthDAO authDao = new AuthDAO();
    private final GameDAO gameDao = new GameDAO();

    public WebSocketHandler() throws ResponseException {
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, ResponseException {
        UserGameCommand userCommand = Deserializer.parse(message, UserGameCommand.class);

        AuthToken authToken;

        try {
            authToken = authDao.getByTokenString(
                    userCommand.getAuthString()
            );
        } catch (ResponseException e) {
            respondWithError(session, "invalid auth token");
            return;
        }

        int gameId = userCommand.getGameID();
        ChessMove move = userCommand.getMove();
        ChessGame.TeamColor color = userCommand.getPlayerColor();

        switch (userCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(authToken, session, gameId, color);
            case JOIN_OBSERVER -> joinObserver(authToken, session, gameId);
            case MAKE_MOVE -> makeMove(authToken, session, gameId);
            case LEAVE -> leave(authToken, session, gameId);
            case RESIGN -> resign(authToken, session, gameId);
        }
    }

    @OnWebSocketError
    public void onError(Session _session, Throwable throwable) {
        throwable.printStackTrace();
    }

    private void respondWithError(Session session, String errorMessage) throws IOException {
        session.getRemote().sendString(Stringifier.jsonify(
                new ServerMessage(
                        ServerMessage.ServerMessageType.ERROR, null, "Error: " + errorMessage, null)
        ));
    }

    private void broadcastLoadGame(String username, String serializedGame) throws IOException {
        connections.singleBroadcast(username, new ServerMessage(
                ServerMessage.ServerMessageType.LOAD_GAME, null, null, serializedGame)
        );
    }

    private void broadcastNotification(String excludeUsername, String message, int gameId) throws IOException {
        var notification = new ServerMessage(
                ServerMessage.ServerMessageType.NOTIFICATION,
                message,
                null, null
        );
        connections.broadcastExcluding(excludeUsername, notification, gameId);
    }

    private void joinPlayer(AuthToken authToken, Session session, int gameId, ChessGame.TeamColor color) throws IOException {
        String username = authToken.username();
        connections.remove(username); // just in case

        try {
            if (color != WHITE && color != BLACK) {
                throw new ResponseException(500, "invalid player color");
            }

            Game game = gameDao.findById(gameId);

            if ((color == WHITE && game.whiteUsername() == null) ||
                    (color == BLACK && game.blackUsername() == null)) {
                throw new ResponseException(500, "team not joined");
            }

            if (
                    (color == WHITE && !Objects.equals(game.whiteUsername(), username) ||
                            (color == BLACK && !Objects.equals(game.blackUsername(), username)))) {
                throw new ResponseException(500, "wrong team");
            }

            connections.add(username, session, gameId);

            broadcastLoadGame(username, Stringifier.jsonify(game));
            broadcastNotification(
                    username,
                    String.format("%s is joining as %s", username, color),
                    gameId
            );

        } catch (ResponseException e) {
            respondWithError(session, e.getMessage());
        }
    }

    private void joinObserver(AuthToken authToken, Session session, int gameId) throws IOException {
        String username = authToken.username();
        connections.remove(username); // just in case

        try {
            Game game = gameDao.findById(gameId);

            connections.add(username, session, gameId);

            broadcastLoadGame(username, Stringifier.jsonify(game));
            broadcastNotification(
                    username,
                    String.format("%s started watching the game", username),
                    gameId
            );
        } catch (ResponseException e) {
            respondWithError(session, "invalid game ID");
        }
    }

    private void makeMove(AuthToken authToken, Session session, int gameId) throws IOException {
        connections.add(authToken.username(), session, gameId);
        var message = String.format("%s is in the shop", authToken);
//        var notification = new ServerMessage(ServerMessage.Type.ARRIVAL, message);
//        connections.broadcast(authToken.username(), notification);
    }


    private void leave(AuthToken authToken, Session session, int gameId) throws IOException {
        String username = authToken.username();

        var currentConn = connections.get(username);

        if (currentConn == null || currentConn.gameId != gameId) {
            respondWithError(session, "can't leave a game you're not participating in");
        } else {
            broadcastNotification(
                    username,
                    String.format("%s left the game for now", username),
                    gameId
            );
        }

        connections.remove(username); // just in case
    }


    private void resign(AuthToken authToken, Session session, int gameId) throws IOException {
        String username = authToken.username();

        var currentConn = connections.get(username);

        try {
            if (currentConn == null || currentConn.gameId != gameId) {
                throw new ResponseException(500, "can't resign from a game you're not participating in");
            }

            gameDao.updateStatus(gameId, Game.Status.OVER);

            broadcastNotification(
                    username,
                    String.format("%s has resigned", username),
                    gameId
            );

        } catch (ResponseException e) {
            respondWithError(session, "invalid game ID");
        }


        connections.remove(username); // just in case
    }

}