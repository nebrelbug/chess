package passoffTests.serviceTests;

import dataAccess.DataAccessException;
import exceptions.ResponseException;
import models.AuthToken;
import models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.ClearDbService;
import services.CreateGameService;
import services.ListGamesService;
import services.RegisterService;

import java.util.ArrayList;


public class ListGamesServiceTest {

    static String username = "List Games";
    static String password = "password";
    static String email = "test@example.com";

    static AuthToken auth;
    static int gameID1;
    static int gameID2;

    @BeforeAll
    public static void before() throws ResponseException {
        ClearDbService.clear();

        auth = new RegisterService().register(username, password, email);

        gameID1 = CreateGameService.create(auth.authToken(), "Game 1");
        gameID2 = CreateGameService.create(auth.authToken(), "Game 2");
    }

    @Test
    public void positiveTest() throws ResponseException {
        ArrayList<Game> games = ListGamesService.listGames(auth.authToken());

        Assertions.assertEquals(games.size(), 2);
        Assertions.assertEquals(games.get(0).gameID(), gameID1);
    }

    @Test
    public void negativeTest() {

        Assertions.assertThrows(ResponseException.class, () -> {
            ListGamesService.listGames("fake auth token"
            );
        });
    }

}
