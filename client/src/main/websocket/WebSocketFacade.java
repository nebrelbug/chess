package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exceptions.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.*;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint implements AutoCloseable {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);

                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


    private void sendCommand(String authToken, UserGameCommand.CommandType commandType, int gameID, ChessGame.TeamColor color, ChessMove move) throws ResponseException {
        try {
            var action = new UserGameCommand(authToken, commandType, gameID, color, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinPlayer(String authToken, int gameId, ChessGame.TeamColor color) throws ResponseException {
        sendCommand(authToken, JOIN_PLAYER, gameId, color, null);
    }

    public void joinObserver(String authToken, int gameId) throws ResponseException {
        sendCommand(authToken, JOIN_OBSERVER, gameId, null, null);
    }

    public void makeMove(String authToken, int gameId, ChessMove move) throws ResponseException {
        sendCommand(authToken, MAKE_MOVE, gameId, null, move);
    }

    public void leave(String authToken, int gameId) throws ResponseException {
        sendCommand(authToken, LEAVE, gameId, null, null);
    }

    public void resign(String authToken, int gameId) throws ResponseException {
        sendCommand(authToken, RESIGN, gameId, null, null);
    }

    @Override
    public void close() throws ResponseException {
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
        } catch (IOException e) {
            // Handle or log the IOException during close
            throw new ResponseException(400, "UH OH SOMETHING BAD");
        }
    }

}
