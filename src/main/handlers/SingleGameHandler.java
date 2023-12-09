package handlers;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import exceptions.ResponseException;
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

            var dao = new GameDAO();
            Game game = dao.findById(id);
            dao.close();

            result.status(200);
            return Stringifier.jsonify(game);

        } catch (ResponseException e) {
            result.status(e.code);

            return Stringifier.jsonify(new ErrorResponse(e.getMessage()));
        }
    }
}
