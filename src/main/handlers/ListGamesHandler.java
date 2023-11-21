package handlers;

import dataAccess.DataAccessException;
import models.Game;
import response.ErrorResponse;
import response.Stringifier;
import services.ListGamesService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

public class ListGamesHandler {

    record Result(ArrayList<Game> games) {
    }

    public static String handleRequest(Request request, Response result) {
        result.type("application/json");

        String tokenString = request.headers("Authorization");

        try {
            ArrayList<Game> games = ListGamesService.listGames(tokenString);

            result.status(200);
            return Stringifier.jsonify(new Result(games));

        } catch (DataAccessException e) {
            result.status(e.code);

            return Stringifier.jsonify(new ErrorResponse(e.getMessage()));
        }
    }
}
