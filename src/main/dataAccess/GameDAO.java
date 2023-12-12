package dataAccess;

import chess.BenChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import exceptions.ResponseException;
import models.Game;
import response.Stringifier;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static models.Game.Status.OVER;
import static models.Game.Status.PLAYING;

/**
 * Object to interface with games in the relational database
 */
public class GameDAO {

    Connection conn;

    public GameDAO() throws ResponseException {
        this.conn = new Database().getConnection();
    }

    public int create(String gameName) throws ResponseException {

        try (var preparedStatement = conn.prepareStatement("INSERT INTO games (white_username, black_username, status, name, board_state) VALUES(?, ?, ?, ?, ?)", RETURN_GENERATED_KEYS)) {
            preparedStatement.setNull(1, Types.VARCHAR);
            preparedStatement.setNull(2, Types.VARCHAR);
            preparedStatement.setString(3, PLAYING.toString());
            preparedStatement.setString(4, gameName);
            var initialState = new BenChessGame();
            initialState.initialize();
            preparedStatement.setString(5, Stringifier.jsonify(initialState));

            preparedStatement.executeUpdate();

            var resultSet = preparedStatement.getGeneratedKeys();

            var ID = 0;
            if (resultSet.next()) {
                ID = resultSet.getInt(1);
            }

            return ID;

        } catch (SQLException e) {
            throw new ResponseException(500, "Server error: failed to create game (" + e.getMessage() + ")");
        }
    }

    public Game findById(int id) throws ResponseException {

        try (var preparedStatement = conn.prepareStatement("SELECT white_username, black_username, status, name, board_state FROM games WHERE id=?")) {

            preparedStatement.setInt(1, id);

            try (var rs = preparedStatement.executeQuery()) {
                if (!rs.next()) throw new ResponseException(401, "Game not found");

                var whiteUsername = rs.getString("white_username");
                var blackUsername = rs.getString("black_username");
                var statusString = rs.getString("status");
                var gameName = rs.getString("name");
                var boardState = rs.getString("board_state");

                BenChessGame board = models.Deserializer.parse(boardState, BenChessGame.class);

                Game.Status status = switch (statusString) {
                    case "OVER" -> OVER;
                    case "PLAYING" -> PLAYING;
                    default -> throw new ResponseException(500, "Invalid status code");
                };

                return new Game(id, whiteUsername, blackUsername, status, gameName, board);
            }
        } catch (SQLException e) {
            throw new ResponseException(500, e.toString());
        }
    }

    public void updateStatus(int gameId, Game.Status status) throws ResponseException {
        try (var preparedStatement = conn.prepareStatement("UPDATE games SET status=? WHERE id=?")) {
            preparedStatement.setString(1, status.toString());
            preparedStatement.setInt(2, gameId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResponseException(500, "Server error: failed to update status");
        }
    }

    public void claimSpot(int gameID, String username, String color) throws ResponseException {

        Game game;

        try {
            game = findById(gameID);
        } catch (ResponseException e) {
            throw new ResponseException(400, "Invalid game ID");
        }

        if (color == null) return;

        if ((color.equals("WHITE") && Objects.equals(game.whiteUsername(), username)) ||
                (color.equals("BLACK") && Objects.equals(game.blackUsername(), username))) {
            return;
        }

        if (color.equals("WHITE") && game.whiteUsername() == null) {
            try (var preparedStatement = conn.prepareStatement("UPDATE games SET white_username=? WHERE id=?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new ResponseException(500, "Server error: failed to claim spot");
            }
        } else if (color.equals("BLACK") && game.blackUsername() == null) {
            try (var preparedStatement = conn.prepareStatement("UPDATE games SET black_username=? WHERE id=?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new ResponseException(500, "Server error: failed to claim spot");
            }
        } else if (color.equals("WHITE") || color.equals("BLACK")) {
            throw new ResponseException(403, "Server error: spot already taken");
        }

        // Otherwise, we're chilling!

    }

    public void makeMove(int gameId, ChessMove move) throws ResponseException {
        Game game = findById(gameId); // make sure the game exists

        if (game.status() == OVER) {
            throw new ResponseException(500, "This game is over; you can't make a move");
        }

        try {
            game.game().makeMove(move);

            updateGame(gameId, Stringifier.jsonify(game.game()));

        } catch (InvalidMoveException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void updateGame(int gameID, String newGame) throws ResponseException {

        findById(gameID); // make sure the game exists

        try (var preparedStatement = conn.prepareStatement("UPDATE games SET board_state=? WHERE id=?")) {
            preparedStatement.setString(1, newGame);
            preparedStatement.setInt(2, gameID);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResponseException(500, "Server error: can't update board state");
        }
    }

    public ArrayList<Game> listGames() throws ResponseException {
        ArrayList<Game> games = new ArrayList<>();

        try (var preparedStatement = conn.prepareStatement("SELECT id, white_username, black_username, status, name, board_state FROM games")) {
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var id = rs.getInt("id");
                    var whiteUsername = rs.getString("white_username");
                    var blackUsername = rs.getString("black_username");
                    var statusString = rs.getString("status");
                    var name = rs.getString("name");
                    var boardState = rs.getString("board_state");

                    BenChessGame board = models.Deserializer.parse(boardState, BenChessGame.class);

                    Game.Status status = switch (statusString) {
                        case "OVER" -> OVER;
                        case "PLAYING" -> PLAYING;
                        default -> throw new ResponseException(500, "Invalid status code");
                    };

                    games.add(new Game(id, whiteUsername, blackUsername, status, name, board));
                }
            }
        } catch (SQLException e) {
            throw new ResponseException(500, e.toString());
        }

        return games;
    }

    public void clear() throws ResponseException {
        try (var createTableStatement = conn.prepareStatement("DELETE FROM games")) {
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResponseException(500, "Error clearing games");
        }

        try (var createTableStatement = conn.prepareStatement("ALTER TABLE games AUTO_INCREMENT = 1")) {
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResponseException(500, "Error resetting games index");
        }
    }

    public void close() throws ResponseException {
        new Database().closeConnection(this.conn);
    }
}
