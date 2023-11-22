package handlers;

import exceptions.ResponseException;
import models.AuthToken;
import response.ErrorResponse;
import response.Stringifier;
import services.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    RegisterService service;

    public RegisterHandler() throws ResponseException {
        service = new RegisterService();
    }

    record UserInfo(String username, String password, String email) {
    }

    public String handleRequest(Request request, Response result) {
        result.type("application/json");

        try {
            UserInfo userInfo = models.Deserializer.parse(request.body(), UserInfo.class);

            AuthToken newAuthToken = service.register(userInfo.username, userInfo.password, userInfo.email);

            result.status(200);
            return Stringifier.jsonify(newAuthToken);

        } catch (ResponseException e) {
            result.status(e.code);

            return Stringifier.jsonify(new ErrorResponse(e.getMessage()));
        }
    }
}
