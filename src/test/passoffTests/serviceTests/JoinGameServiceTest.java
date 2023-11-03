package passoffTests.serviceTests;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
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
    public static void before() throws DataAccessException {
        ClearDbService.clear();
        auth = RegisterService.register(username, password, email);
        gameID = CreateGameService.create(auth.authToken(), "New Game");

        auth2 = RegisterService.register(username + "1", password + "1", email + "1");
        auth3 = RegisterService.register(username + "2", password + "2", email + "2");
        auth4 = RegisterService.register(username + "3", password + "3", email + "3");
    }

    @Test
    public void positiveTest() throws DataAccessException {
        JoinGameService.join(auth2.authToken(), gameID, "WHITE");
        JoinGameService.join(auth3.authToken(), gameID, "BLACK");
    }

    @Test
    public void negativeTest() {

        Assertions.assertThrows(DataAccessException.class, () -> {
            JoinGameService.join(auth3.authToken(), gameID, "BLACK");
        });

        Assertions.assertThrows(DataAccessException.class, () -> {
            JoinGameService.join(auth4.authToken(), gameID, "BLACK");
        });
    }

}
