package handlers;

import dataAccess.DataAccessException;
import models.Game;
import request.RequestParser;
import response.ErrorResponse;
import response.Stringifier;
import services.CreateGameService;
import services.ListGamesService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

public class CreateGameHandler {

    record Result(int gameID) {
    }

    record Body(String gameName) {
    }

    public static String handleRequest(Request request, Response result) {
        result.type("application/json");

        try {
            Body body = RequestParser.parse(request.body(), Body.class);
            String tokenString = request.headers("Authorization");

            int gameID = CreateGameService.create(tokenString, body.gameName);

            result.status(200);
            return Stringifier.jsonify(new Result(gameID));

        } catch (DataAccessException e) {
            result.status(e.code);

            return Stringifier.jsonify(new ErrorResponse(e.getMessage()));
        }
    }
}