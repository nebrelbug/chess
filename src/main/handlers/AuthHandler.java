package handlers;

import com.google.gson.Gson;
import models.AuthToken;
import services.AuthService;
import spark.Request;
import spark.Response;

public class AuthHandler {

    record Credentials(String username, String password) {
    }

    record Result(String username, AuthToken authToken) {
    }

    public static String handleLoginRequest(Request request, Response result) {

        Gson gson = new Gson();

        result.type("application/json");

        Credentials creds = gson.fromJson(request.body(), Credentials.class);

        try {
            AuthToken newAuthToken = AuthService.login(creds.username, creds.password);


            result.status(200);
        } catch (Exception e) {
            result.status(500);
            return new Gson().toJson(new Object());
        }

    }
}
