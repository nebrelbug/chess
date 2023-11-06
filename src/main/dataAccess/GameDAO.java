package dataAccess;

import chess.BenChessBoard;
import chess.BenChessGame;
import chess.ChessGame;
import models.AuthToken;
import models.Game;
import models.User;
import request.RequestParser;
import response.Stringifier;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * Object to interface with games in the relational database
 */
public class GameDAO {

    Connection conn;

    public GameDAO() throws DataAccessException {
        this.conn = new Database().getConnection();
    }

    public int create(String gameName) throws DataAccessException {

        try (var preparedStatement = conn.prepareStatement("INSERT INTO games (white_username, black_username, name, board_state) VALUES(?, ?, ?, ?)", RETURN_GENERATED_KEYS)) {
            preparedStatement.setNull(1, Types.VARCHAR);
            preparedStatement.setNull(2, Types.VARCHAR);
            preparedStatement.setString(3, gameName);
            preparedStatement.setString(4, Stringifier.jsonify(new BenChessGame()));

            preparedStatement.executeUpdate();

            var resultSet = preparedStatement.getGeneratedKeys();

            var ID = 0;
            if (resultSet.next()) {
                ID = resultSet.getInt(1);
            }

            return ID;

        } catch (SQLException e) {
            throw new DataAccessException(500, "Server error: failed to create game");
        }
    }

    public Game findById(int id) throws DataAccessException {

        try (var preparedStatement = conn.prepareStatement("SELECT white_username, black_username, name, board_state FROM games WHERE id=?")) {

            preparedStatement.setInt(1, id);

            try (var rs = preparedStatement.executeQuery()) {
                if (!rs.next()) throw new DataAccessException(401, "Game not found");

                var whiteUsername = rs.getString("white_username");
                var blackUsername = rs.getString("black_username");
                var gameName = rs.getString("name");
                var boardState = rs.getString("board_state");

                BenChessGame board = RequestParser.parse(boardState, BenChessGame.class);

                return new Game(id, whiteUsername, blackUsername, gameName, board);
            }
        } catch (SQLException e) {
            throw new DataAccessException(500, e.toString());
        }
    }

    public void claimSpot(int gameID, String username, String color) throws DataAccessException {

        Game game;

        try {
            game = findById(gameID);
        } catch (DataAccessException e) {
            throw new DataAccessException(400, "Invalid game ID");
        }

        if (color == null) return;

        if (color.equals("WHITE") && game.whiteUsername() == null) {
            try (var preparedStatement = conn.prepareStatement("UPDATE games SET white_username=? WHERE id=?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(500, "Server error: failed to claim spot");
            }
        } else if (color.equals("BLACK") && game.blackUsername() == null) {
            try (var preparedStatement = conn.prepareStatement("UPDATE games SET black_username=? WHERE id=?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(500, "Server error: failed to claim spot");
            }
        } else if (color.equals("WHITE") || color.equals("BLACK")) {
            throw new DataAccessException(403, "Server error: spot already taken");
        }

        // Otherwise, we're chilling!

    }

    public void updateGame(int gameID, String newGame) throws DataAccessException {

        findById(gameID); // make sure the game exists

        try (var preparedStatement = conn.prepareStatement("UPDATE games SET board_state=? WHERE id=?")) {
            preparedStatement.setString(1, newGame);
            preparedStatement.setInt(2, gameID);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(500, "Server error: can't update board state");
        }
    }

    public ArrayList<Game> listGames() throws DataAccessException {
        ArrayList<Game> games = new ArrayList<>();

        try (var preparedStatement = conn.prepareStatement("SELECT id, white_username, black_username, name, board_state FROM games")) {
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var id = rs.getInt("id");
                    var whiteUsername = rs.getString("white_username");
                    var blackUsername = rs.getString("black_username");
                    var name = rs.getString("name");
                    var boardState = rs.getString("board_state");

                    BenChessGame board = RequestParser.parse(boardState, BenChessGame.class);

                    games.add(new Game(id, whiteUsername, blackUsername, name, board));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(500, e.toString());
        }

        return games;
    }

    public void clear() throws DataAccessException {
        try (var createTableStatement = conn.prepareStatement("DELETE FROM games")) {
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(500, "Error clearing games");
        }

        try (var createTableStatement = conn.prepareStatement("ALTER TABLE games AUTO_INCREMENT = 1")) {
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(500, "Error resetting games index");
        }
    }
}
