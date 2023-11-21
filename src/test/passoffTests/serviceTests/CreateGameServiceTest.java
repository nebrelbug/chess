package passoffTests.serviceTests;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import exceptions.ResponseException;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.ClearDbService;
import services.CreateGameService;
import services.RegisterService;


public class CreateGameServiceTest {

    static String username = "Create Game";
    static String password = "password";
    static String email = "test@example.com";

    static AuthToken auth;

    @BeforeAll
    public static void before() throws ResponseException {
        ClearDbService.clear();
        auth = new RegisterService().register(username, password, email);
    }

    @Test
    public void positiveTest() throws ResponseException {

        int gameID = CreateGameService.create(auth.authToken(), "New Game");

        new GameDAO().findById(gameID);

        Assertions.assertEquals(gameID, 1);

        int gameID2 = CreateGameService.create(auth.authToken(), "New Game");

        Assertions.assertEquals(gameID2, 2);
    }

    @Test
    public void negativeTest() {

        Assertions.assertThrows(ResponseException.class, () -> {
            CreateGameService.create("invalid token", "New Game");
        });
    }

}
