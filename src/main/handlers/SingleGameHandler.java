package handlers;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Game;
import response.ErrorResponse;
import response.Stringifier;
import spark.Request;
import spark.Response;

public class SingleGameHandler {

    public static String handleRequest(Request request, Response result) {
        result.type("application/json");

        try {
//            String tokenString = request.headers("Authorization");

            int id = Integer.parseInt(request.params(":id"));

            Game game = new GameDAO().findById(id);

            result.status(200);
            return Stringifier.jsonify(game);

        } catch (DataAccessException e) {
            result.status(e.code);

            return Stringifier.jsonify(new ErrorResponse(e.getMessage()));
        }
    }
}
