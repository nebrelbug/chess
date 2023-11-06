package passoffTests.daoTests;

import chess.*;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Game;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RequestParser;
import response.Stringifier;


public class GameDaoTest {

    GameDAO dao;

    public GameDaoTest() throws DataAccessException {
        this.dao = new GameDAO();
    }

    @BeforeEach
    public void before() throws DataAccessException {
        dao.clear();
    }

    @Test
    public void createPositiveTest() throws DataAccessException {
        dao.create("gamename");
        Assertions.assertEquals(dao.listGames().size(), 1);
    }

    @Test
    public void createNegativeTest() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            dao.create(null);
        });
    }

    @Test
    public void findByIdPositiveTest() throws DataAccessException {
        int id = dao.create("gamename");
        Game game = dao.findById(id);

        Assertions.assertNull(game.whiteUsername());
        Assertions.assertNull(game.blackUsername());
        Assertions.assertEquals("gamename", game.gameName());
    }

    @Test
    public void findByIdNegativeTest() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            dao.findById(0);
        });
    }

    @Test
    public void claimSpotPositiveTest() throws DataAccessException {
        int id = dao.create("gamename");
        dao.claimSpot(id, "fakeuser", "WHITE");
        Game game = dao.findById(id);

        Assertions.assertEquals("fakeuser", game.whiteUsername());
    }

    @Test
    public void claimSpotNegativeTest() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            int id = dao.create("gamename");
            dao.claimSpot(id, "fakeuser", "WHITE");
            dao.claimSpot(id, "anotheruser", "WHITE");
            // already taken
        });
    }

    @Test
    public void updateGamePositiveTest() throws DataAccessException {
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
        Assertions.assertThrows(DataAccessException.class, () -> {
            Game game = new Game(3, "a", "b", "name", new BenChessGame());
            dao.updateGame(0, Stringifier.jsonify(game.game()));
        });
    }

    @Test
    public void listGamesPositiveTest() throws DataAccessException {
        dao.create("game1");
        dao.create("game2");
        dao.create("game3");

        var games = dao.listGames();
        Assertions.assertEquals(3, games.size());
    }

    @Test
    public void listGamesNegativeTest() throws DataAccessException {
        var games = dao.listGames();
        Assertions.assertEquals(0, games.size());
    }

    @Test
    public void clearTest() throws DataAccessException {
        dao.create("game1");
        dao.create("game2");
        dao.create("game3");

        Assertions.assertEquals(3, dao.listGames().size());

        dao.clear();
        Assertions.assertEquals(0, dao.listGames().size());
    }
}

