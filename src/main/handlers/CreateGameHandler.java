package handlers;

import exceptions.ResponseException;
import request.RequestParser;
import response.ErrorResponse;
import response.Stringifier;
import services.CreateGameService;
import spark.Request;
import spark.Response;

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

        } catch (ResponseException e) {
            result.status(e.code);

            return Stringifier.jsonify(new ErrorResponse(e.getMessage()));
        }
    }
}
