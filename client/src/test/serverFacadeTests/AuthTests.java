package serverFacadeTests;

import exceptions.ResponseException;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ServerFacade;

import java.net.*;

public class AuthTests {

    ServerFacade server = new ServerFacade("http://localhost:8080");

    @BeforeEach
    public void setup() throws Exception {

        try {
            URL url = new URL("http://localhost:8080/db");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            int responseCode = connection.getResponseCode();
            if (responseCode / 100 != 2) throw new Exception("Failed to clear database");

            System.out.println("Cleared Database");
        } catch (Exception e) {
            throw new Exception("Failed to clear database");
        }
    }

    @Test
    public void loginPositiveTest() throws ResponseException {
        var token1 = server.register("a", "b", "c@d");
        var token2 = server.login("a", "b");

        Assertions.assertEquals(36, token1.authToken().length());
        Assertions.assertEquals(36, token2.authToken().length());
        Assertions.assertEquals(token1.username(), token2.username());
    }

    @Test
    public void loginNegativeTest() {
        Assertions.assertThrows(ResponseException.class, () -> {
            server.register("a", "b", "c@d");
            server.login("a", "f");
        });
    }

    @Test
    public void logoutPositiveTest() throws ResponseException {
        server.register("a", "b", "c@d");
        AuthToken token = server.login("a", "b");
        server.logout(token.authToken());

        Assertions.assertThrows(ResponseException.class, () -> server.create(token.authToken(), "newgame"));

    }

    @Test
    public void logoutNegativeTest() {
        Assertions.assertThrows(ResponseException.class, () -> {
            server.register("a", "b", "c@d");
            server.logout("nonexistent-token");
        });
    }

    @Test
    public void registerPositiveTest() throws ResponseException {
        AuthToken token = server.register("a", "b", "c@d");
        Assertions.assertEquals(36, token.authToken().length());
    }

    @Test
    public void registerNegativeTest() throws ResponseException {
        AuthToken token = server.register("a", "b", "c@d");
        server.logout(token.authToken());

        // throws if the username already exists
        Assertions.assertThrows(ResponseException.class, () -> server.register("a", "b", "c@d"));
    }
}
