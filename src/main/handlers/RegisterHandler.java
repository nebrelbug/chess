package handlers;

import com.google.gson.Gson;
import request.RequestParser;
import dataAccess.DataAccessException;
import models.AuthToken;
import response.ErrorResponse;
import response.Stringifier;
import services.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    record UserInfo(String username, String password, String email) {
    }

    public static String handleRequest(Request request, Response result) {
        result.type("application/json");

        try {
            UserInfo userInfo = RequestParser.parse(request.body(), UserInfo.class);

            AuthToken newAuthToken = RegisterService.register(userInfo.username, userInfo.password, userInfo.email);

            result.status(200);
            return Stringifier.jsonify(newAuthToken);

        } catch (DataAccessException e) {
            result.status(e.code);

            return Stringifier.jsonify(new ErrorResponse(e.getMessage()));
        }
    }
}
