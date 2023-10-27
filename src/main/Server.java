import spark.*;
import com.google.gson.Gson;

import java.util.*;

public class Server {

    private ArrayList<String> names = new ArrayList<>();

    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {
        // Specify the port you want the server to listen on
        Spark.port(8080);

        // Register a directory for hosting static files
        Spark.externalStaticFileLocation("web");

        Spark.delete("/db", this::dbDelete);

        Spark.post("/user", this::dbDelete);

        Spark.post("/session", this::dbDelete);

        Spark.delete("/session", this::dbDelete);

        Spark.get("/game", this::dbDelete);

        Spark.post("/game", this::dbDelete);


        // Register handlers for each endpoint using the method reference syntax
        Spark.post("/name/:name", this::addName);
        Spark.get("/name", this::listNames);
    }

    private Object addName(Request req, Response res) {
        names.add(req.params(":name"));
        return listNames(req, res);
    }

    private String listNames(Request req, Response res) {
        res.type("application/json");
        return new Gson().toJson(Map.of("name", names));
    }

    private Object dbDelete(Request req, Response res) {
        res.type("application/json");
        res.status(200);
        return new Gson().toJson(Map.of("name", names));
    }
}
