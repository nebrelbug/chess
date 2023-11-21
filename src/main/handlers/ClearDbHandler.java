package handlers;

import response.ErrorResponse;
import response.Stringifier;
import services.ClearDbService;
import spark.Request;
import spark.Response;

public class ClearDbHandler {

    public static String handleRequest(Request request, Response result) {

        result.type("application/json");

        try {
            ClearDbService.clear();
            result.status(200);
            return Stringifier.jsonify(new Object());
        } catch (Exception e) {
            result.status(500);
        }

        return Stringifier.jsonify(new ErrorResponse("Error: description"));
    }
}
