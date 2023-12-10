package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import response.Stringifier;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session, int gameId) {
        var connection = new Connection(visitorName, session, gameId);
        connections.put(visitorName, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public Connection get(String visitorName) {
        return connections.get(visitorName);
    }

    public void broadcastExcluding(String excludeVisitorName, ServerMessage notification, int gameId) throws IOException {

        String notificationString = new Gson().toJson(notification);

        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.gameId == gameId && !c.visitorName.equals(excludeVisitorName)) {
                    c.send(notificationString);
                    System.out.println("Broadcasting to: " + c.visitorName);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }

    public void singleBroadcast(String visitorName, ServerMessage notification) throws IOException {

        String notificationString = Stringifier.jsonify(notification);

        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.visitorName.equals(visitorName)) {
                    c.send(notificationString);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }
}