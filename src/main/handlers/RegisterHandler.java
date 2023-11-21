package handlers;

import request.RequestException;
import request.RequestParser;
import dataAccess.DataAccessException;
import models.AuthToken;
import response.ErrorResponse;
import response.Stringifier;
import services.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    RegisterService service;

    public RegisterHandler() throws DataAccessException {
        service = new RegisterService();
    }

    record UserInfo(String username, String password, String email) {
    }

    public String handleRequest(Request request, Response result) {
        result.type("application/json");

        try {
            UserInfo userInfo = RequestParser.parse(request.body(), UserInfo.class);

            AuthToken newAuthToken = service.register(userInfo.username, userInfo.password, userInfo.email);

            result.status(200);
            return Stringifier.jsonify(newAuthToken);

        } catch (DataAccessException e) {
            result.status(e.code);

            return Stringifier.jsonify(new ErrorResponse(e.getMessage()));
        } catch (RequestException e) {
            result.status(400);

            return Stringifier.jsonify(new ErrorResponse(e.getMessage()));
        }
    }
}
