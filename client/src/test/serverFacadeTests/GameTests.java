package serverFacadeTests;

import chess.ChessGame;
import exceptions.ResponseException;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ServerFacade;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class GameTests {

    ServerFacade server = new ServerFacade("http://localhost:8080");

    AuthToken token;

    @BeforeEach
    public void setup() throws Exception {

        try {
            URL url = new URL("http://localhost:8080/db");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            int responseCode = connection.getResponseCode();

            if (responseCode / 100 != 2) throw new Exception("Failed to clear database");

            token = server.register("a", "b", "c@d");
        } catch (IOException e) {
            throw new Exception("Failed to clear database: " + e.getMessage());
        }
    }

    @Test
    public void createGamePositiveTest() throws ResponseException {
        int id1 = server.create(token.authToken(), "Sweet Game");
        int id2 = server.create(token.authToken(), "Sweet Game");

        Assertions.assertEquals(1, id1);
        Assertions.assertEquals(2, id2);
    }

    @Test
    public void createGameNegativeTest() {
        Assertions.assertThrows(ResponseException.class, () -> server.create("invalid token", "Sweet Game"));
        Assertions.assertThrows(ResponseException.class, () -> server.create(token.authToken(), null));
    }

    @Test
    public void listGamesPositiveTest() throws ResponseException {
        server.create(token.authToken(), "Game 1");
        server.create(token.authToken(), "Game 2");

        var games = server.list(token.authToken());

        Assertions.assertEquals(2, games.size());
    }


    @Test
    public void listGamesNegativeTest() throws ResponseException {
        server.create(token.authToken(), "Game 1");
        server.create(token.authToken(), "Game 2");

        Assertions.assertThrows(ResponseException.class, () -> server.list("invalid auth token"));
    }

    @Test
    public void joinGamePositiveTest() throws ResponseException {
        int gameId1 = server.create(token.authToken(), "Game 1");
        int gameId2 = server.create(token.authToken(), "Game 2");

        server.join(token.authToken(), gameId1, ChessGame.TeamColor.WHITE); // joining
        server.join(token.authToken(), gameId2, null); // observing

        var game1 = server.getGame(token.authToken(), gameId1);
        var game2 = server.getGame(token.authToken(), gameId2);

        Assertions.assertEquals("a", game1.whiteUsername());
        Assertions.assertNull(game2.whiteUsername());
    }

    @Test
    public void joinGameNegativeTest() throws ResponseException {
        int gameId1 = server.create(token.authToken(), "Game 1");

        server.join(token.authToken(), gameId1, ChessGame.TeamColor.WHITE); // joining

        Assertions.assertThrows(ResponseException.class, () -> {
            server.join(token.authToken(), gameId1, ChessGame.TeamColor.WHITE); // spot already taken
        });
    }

    @Test
    public void getGamePositiveTest() throws ResponseException {
        int gameId1 = server.create(token.authToken(), "Game 1");

        server.join(token.authToken(), gameId1, ChessGame.TeamColor.WHITE); // joining

        var game = server.getGame(token.authToken(), gameId1);

        Assertions.assertEquals("Game 1", game.gameName());
        Assertions.assertEquals("a", game.whiteUsername());
        Assertions.assertNull(game.blackUsername());
    }

    @Test
    public void getGameNegativeTest() throws ResponseException {
        int gameId1 = server.create(token.authToken(), "Game 1");

        Assertions.assertThrows(ResponseException.class, () -> {
            server.getGame(token.authToken(), gameId1 + 1); // game ID doesn't exist
        });
    }
}
