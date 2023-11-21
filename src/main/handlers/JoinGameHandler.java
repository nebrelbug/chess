package handlers;

import dataAccess.DataAccessException;
import request.RequestException;
import request.RequestParser;
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
            Body body = RequestParser.parse(request.body(), Body.class);
            String tokenString = request.headers("Authorization");

            JoinGameService.join(tokenString, body.gameID, body.playerColor);

            result.status(200);
            return Stringifier.jsonify(new Object());

        } catch (DataAccessException e) {
            result.status(e.code);

            return Stringifier.jsonify(new ErrorResponse(e.getMessage()));
        } catch (RequestException e) {
            result.status(400);

            return Stringifier.jsonify(new ErrorResponse(e.getMessage()));
        }
    }
}
