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
import static models.Game.Status.OVER;

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
            case MAKE_MOVE -> makeMove(authToken, session, gameId, move);
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

    private void broadcastSingleLoadGame(String username, String serializedGame) throws IOException {
        connections.singleBroadcast(username, new ServerMessage(
                ServerMessage.ServerMessageType.LOAD_GAME, null, null, serializedGame)
        );
    }

    private void broadcastLoadGame(String excluding, String serializedGame, int gameId) throws IOException {

        var notification = new ServerMessage(
                ServerMessage.ServerMessageType.LOAD_GAME, null, null, serializedGame);

        connections.broadcastExcluding(excluding, notification, gameId);
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

            broadcastSingleLoadGame(username, Stringifier.jsonify(game));
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

            broadcastSingleLoadGame(username, Stringifier.jsonify(game));
            broadcastNotification(
                    username,
                    String.format("%s started watching the game", username),
                    gameId
            );
        } catch (ResponseException e) {
            respondWithError(session, "invalid game ID");
        }
    }

    private void makeMove(AuthToken authToken, Session session, int gameId, ChessMove move) throws IOException {
        String username = authToken.username();

        try {

            Game ogGame;

            try {
                ogGame = gameDao.findById(gameId);
            } catch (ResponseException e) {
                throw new ResponseException(500, "invalid game ID");
            }

            ChessGame.TeamColor playerColor;

            if (Objects.equals(username, ogGame.whiteUsername())) {
                playerColor = WHITE;
            } else if (Objects.equals(username, ogGame.blackUsername())) {
                playerColor = BLACK;
            } else {
                throw new ResponseException(500, "can't move as an observer");
            }

            var pieceToMove = ogGame.game().getBoard().getPiece(move.getStartPosition());

            if (pieceToMove.getTeamColor() != playerColor) {
                throw new ResponseException(500, "can't move the other player's piece");
            }

            gameDao.makeMove(gameId, move);

            Game updatedGame = gameDao.findById(gameId);

            broadcastLoadGame("", Stringifier.jsonify(updatedGame), gameId);
            broadcastNotification(username,
                    String.format("%s made move %s", username, move),
                    gameId
            );

            if (updatedGame.game().isInCheckmate(WHITE)) {
                broadcastNotification("", "White has been checkmated", gameId);
                gameDao.updateStatus(gameId, OVER);
            } else if (updatedGame.game().isInCheckmate(BLACK)) {
                broadcastNotification("", "Black has been checkmated", gameId);
                gameDao.updateStatus(gameId, OVER);
            } else {
                if (updatedGame.game().isInCheck(WHITE)) {
                    broadcastNotification("", "White is in check", gameId);
                } else if (updatedGame.game().isInCheck(BLACK)) {
                    broadcastNotification("", "Black is in check", gameId);
                }
            }
        } catch (ResponseException e) {
            respondWithError(session, e.getMessage());
        }
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

            Game game;

            try {
                game = gameDao.findById(gameId);
            } catch (ResponseException e) {
                throw new ResponseException(500, "invalid game ID");
            }

            if (!Objects.equals(username, game.whiteUsername()) && !Objects.equals(username, game.blackUsername())) {
                throw new ResponseException(500, "can't resign as an observer");
            }

            if (game.status() == OVER) {
                throw new ResponseException(500, "this game is already over");
            }

            gameDao.updateStatus(gameId, OVER);

            broadcastNotification(
                    "",
                    String.format("%s has resigned", username),
                    gameId
            );

            connections.remove(username); // just in case

        } catch (ResponseException e) {
            respondWithError(session, e.getMessage());
        }
    }

}