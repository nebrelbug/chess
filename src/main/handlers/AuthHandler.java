package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import request.RequestParser;
import response.ErrorResponse;
import response.Stringifier;
import services.AuthService;
import spark.Request;
import spark.Response;

import java.util.Set;

public class AuthHandler {

    record Credentials(String username, String password) {
    }

    public static String handleLoginRequest(Request request, Response result) {
        result.type("application/json");

        try {
            Credentials creds = RequestParser.parse(request.body(), Credentials.class);

            AuthToken newAuthToken = AuthService.login(creds.username, creds.password);

            result.status(200);
            return Stringifier.jsonify(newAuthToken);

        } catch (DataAccessException e) {
            result.status(e.code);
            String errorMessage = e.getMessage();
            return Stringifier.jsonify(new ErrorResponse(errorMessage));
        }
    }

    public static String handleLogoutRequest(Request request, Response result) {
        result.type("application/json");

        String tokenString = request.headers("Authorization");

        try {
            AuthService.logout(tokenString);

            result.status(200);
            return Stringifier.jsonify(new Object());

        } catch (DataAccessException e) {
            result.status(e.code);
            String errorMessage = e.getMessage();
            return Stringifier.jsonify(new ErrorResponse(errorMessage));
        }
    }
}
