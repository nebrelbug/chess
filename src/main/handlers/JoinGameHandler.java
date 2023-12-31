package handlers;

import exceptions.ResponseException;
import response.ErrorResponse;
import response.Stringifier;
import services.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {

    record Body(int gameID, String playerColor) {
    }

    public static String handleRequest(Request request, Response result) {
        result.type("application/json");

        try {
            Body body = models.Deserializer.parse(request.body(), Body.class);
            String tokenString = request.headers("Authorization");

            JoinGameService.join(tokenString, body.gameID, body.playerColor);

            result.status(200);
            return Stringifier.jsonify(new Object());

        } catch (ResponseException e) {
            result.status(e.code);

            return Stringifier.jsonify(new ErrorResponse(e.getMessage()));
        }
    }
}
