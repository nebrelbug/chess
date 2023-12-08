package websocket;

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
public class WebSocketFacade extends Endpoint {

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


    private void sendCommand(String authToken, UserGameCommand.CommandType commandType, String data) throws ResponseException {
        try {
            var action = new UserGameCommand(authToken, commandType, data);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinPlayer(String authToken, String color) throws ResponseException {
        sendCommand(authToken, JOIN_PLAYER, color);
    }

    public void joinObserver(String authToken) throws ResponseException {
        sendCommand(authToken, JOIN_OBSERVER, null);
    }

    public void makeMove(String authToken, String move) throws ResponseException {
        sendCommand(authToken, MAKE_MOVE, move);
    }

    public void leave(String authToken) throws ResponseException {
        sendCommand(authToken, LEAVE, null);
    }

    public void resign(String authToken) throws ResponseException {
        sendCommand(authToken, RESIGN, null);
    }

}
