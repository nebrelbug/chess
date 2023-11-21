package handlers;

import dataAccess.DataAccessException;
import models.AuthToken;
import request.RequestException;
import request.RequestParser;
import response.ErrorResponse;
import response.Stringifier;
import services.AuthService;
import spark.Request;
import spark.Response;

public class AuthHandler {

    AuthService service;

    public AuthHandler() throws DataAccessException {
        service = new AuthService();
    }

    record Credentials(String username, String password) {
    }

    public String handleLoginRequest(Request request, Response result) {
        result.type("application/json");

        try {
            Credentials creds = RequestParser.parse(request.body(), Credentials.class);

            AuthToken newAuthToken = service.login(creds.username, creds.password);

            result.status(200);
            return Stringifier.jsonify(newAuthToken);

        } catch (DataAccessException e) {
            result.status(e.code);
            String errorMessage = e.getMessage();
            return Stringifier.jsonify(new ErrorResponse(errorMessage));
        } catch (RequestException e) {
            result.status(400);

            return Stringifier.jsonify(new ErrorResponse(e.getMessage()));
        }
    }

    public String handleLogoutRequest(Request request, Response result) {
        result.type("application/json");

        String tokenString = request.headers("Authorization");

        try {
            service.logout(tokenString);

            result.status(200);
            return Stringifier.jsonify(new Object());

        } catch (DataAccessException e) {
            result.status(e.code);
            String errorMessage = e.getMessage();
            return Stringifier.jsonify(new ErrorResponse(errorMessage));
        }
    }
}
