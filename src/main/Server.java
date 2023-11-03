import spark.*;
import handlers.*;

public class Server {
    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {
        // Specify the port you want the server to listen on
        Spark.port(8080);

        // Register a directory for hosting static files
        Spark.externalStaticFileLocation("web");

        Spark.delete("/db", ClearDbHandler::handleRequest);

        Spark.post("/user", RegisterHandler::handleRequest);

        Spark.post("/session", AuthHandler::handleLoginRequest);

        Spark.delete("/session", AuthHandler::handleLogoutRequest);

        Spark.get("/game", ListGamesHandler::handleRequest);

        Spark.post("/game", CreateGameHandler::handleRequest);

        Spark.put("/game", JoinGameHandler::handleRequest);

    }
}
