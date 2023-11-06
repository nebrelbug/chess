import dataAccess.DataAccessException;
import spark.*;
import handlers.*;

public class Server {
    public static void main(String[] args) throws Exception {

        new DatabaseInit().initialize();
        
        new Server().run();

    }

    private void run() throws DataAccessException {

        var authHandler = new AuthHandler();
        var registerHandler = new RegisterHandler();


        // Specify the port you want the server to listen on
        Spark.port(8080);

        // Register a directory for hosting static files
        Spark.externalStaticFileLocation("web");

        Spark.delete("/db", ClearDbHandler::handleRequest);

        Spark.post("/user", registerHandler::handleRequest);

        Spark.post("/session", authHandler::handleLoginRequest);

        Spark.delete("/session", authHandler::handleLogoutRequest);

        Spark.get("/game", ListGamesHandler::handleRequest);

        Spark.post("/game", CreateGameHandler::handleRequest);

        Spark.put("/game", JoinGameHandler::handleRequest);

        Spark.get("/game/:id", SingleGameHandler::handleRequest);

    }
}
