package passoffTests.daoTests;

import chess.*;
import exceptions.ResponseException;
import dataAccess.GameDAO;
import models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import response.Stringifier;


public class GameDaoTest {

    GameDAO dao;

    public GameDaoTest() throws ResponseException {
        this.dao = new GameDAO();
    }

    @BeforeEach
    public void before() throws ResponseException {
        dao.clear();
    }

    @Test
    public void createPositiveTest() throws ResponseException {
        dao.create("gamename");
        Assertions.assertEquals(dao.listGames().size(), 1);
    }

    @Test
    public void createNegativeTest() {
        Assertions.assertThrows(ResponseException.class, () -> {
            dao.create(null);
        });
    }

    @Test
    public void findByIdPositiveTest() throws ResponseException {
        int id = dao.create("gamename");
        Game game = dao.findById(id);

        Assertions.assertNull(game.whiteUsername());
        Assertions.assertNull(game.blackUsername());
        Assertions.assertEquals("gamename", game.gameName());
    }

    @Test
    public void findByIdNegativeTest() {
        Assertions.assertThrows(ResponseException.class, () -> {
            dao.findById(0);
        });
    }

    @Test
    public void claimSpotPositiveTest() throws ResponseException {
        int id = dao.create("gamename");
        dao.claimSpot(id, "fakeuser", "WHITE");
        Game game = dao.findById(id);

        Assertions.assertEquals("fakeuser", game.whiteUsername());
    }

    @Test
    public void claimSpotNegativeTest() {
        Assertions.assertThrows(ResponseException.class, () -> {
            int id = dao.create("gamename");
            dao.claimSpot(id, "fakeuser", "WHITE");
            dao.claimSpot(id, "anotheruser", "WHITE");
            // already taken
        });
    }

    @Test
    public void updateGamePositiveTest() throws ResponseException {
        int id = dao.create("gamename");
        Game game = dao.findById(id);

        var position = new BenChessPosition(8, 8);
        BenChessBoard board = new BenChessBoard();
        board.addPiece(position, new BenChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        game.game().setBoard(board);

        dao.updateGame(id, Stringifier.jsonify(game.game()));
        Game newGame = dao.findById(id);
        Assertions.assertEquals(board, newGame.game().getBoard());
    }

    @Test
    public void updateGameNegativeTest() {
        Assertions.assertThrows(ResponseException.class, () -> {
            Game game = new Game(3, "a", "b", "name", new BenChessGame());
            dao.updateGame(0, Stringifier.jsonify(game.game()));
        });
    }

    @Test
    public void listGamesPositiveTest() throws ResponseException {
        dao.create("game1");
        dao.create("game2");
        dao.create("game3");

        var games = dao.listGames();
        Assertions.assertEquals(3, games.size());
    }

    @Test
    public void listGamesNegativeTest() throws ResponseException {
        var games = dao.listGames();
        Assertions.assertEquals(0, games.size());
    }

    @Test
    public void clearTest() throws ResponseException {
        dao.create("game1");
        dao.create("game2");
        dao.create("game3");

        Assertions.assertEquals(3, dao.listGames().size());

        dao.clear();
        Assertions.assertEquals(0, dao.listGames().size());
    }
}

