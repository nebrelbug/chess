package passoffTests.serviceTests;

import dataAccess.DataAccessException;
import exceptions.ResponseException;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.ClearDbService;
import services.CreateGameService;
import services.JoinGameService;
import services.RegisterService;


public class JoinGameServiceTest {

    static String username = "Join Game";
    static String password = "password";
    static String email = "test@example.com";

    static int gameID;

    static AuthToken auth;
    static AuthToken auth2;
    static AuthToken auth3;
    static AuthToken auth4;

    @BeforeAll
    public static void before() throws ResponseException {
        ClearDbService.clear();

        var rService = new RegisterService();

        auth = rService.register(username, password, email);
        gameID = CreateGameService.create(auth.authToken(), "New Game");

        auth2 = rService.register(username + "1", password + "1", email + "1");
        auth3 = rService.register(username + "2", password + "2", email + "2");
        auth4 = rService.register(username + "3", password + "3", email + "3");
    }

    @Test
    public void positiveTest() throws ResponseException {
        JoinGameService.join(auth2.authToken(), gameID, "WHITE");
        JoinGameService.join(auth3.authToken(), gameID, "BLACK");
    }

    @Test
    public void negativeTest() {

        Assertions.assertThrows(ResponseException.class, () -> {
            JoinGameService.join(auth3.authToken(), gameID, "BLACK");
        });

        Assertions.assertThrows(ResponseException.class, () -> {
            JoinGameService.join(auth4.authToken(), gameID, "BLACK");
        });
    }

}
