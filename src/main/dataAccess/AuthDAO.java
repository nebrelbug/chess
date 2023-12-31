package dataAccess;

import exceptions.ResponseException;
import models.AuthToken;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Object to interface with AuthTokens in the relational database
 */
public class AuthDAO {

    Connection conn;

    public AuthDAO() throws ResponseException {
        this.conn = new Database().getConnection();
    }

    public AuthToken getByTokenString(String tokenString) throws ResponseException {

        try (var preparedStatement = conn.prepareStatement("SELECT token, username FROM auth_tokens WHERE token=?")) {

            preparedStatement.setString(1, tokenString);

            try (var rs = preparedStatement.executeQuery()) {
                if (!rs.next()) throw new ResponseException(401, "token not found");

                var token = rs.getString("token");
                var username = rs.getString("username");

                return new AuthToken(username, token);
            }
        } catch (SQLException e) {
            throw new ResponseException(500, e.toString());
        }
    }

    public void remove(AuthToken tokenToRemove) throws ResponseException {
        try (var preparedStatement = conn.prepareStatement("DELETE FROM auth_tokens WHERE token=?")) {
            preparedStatement.setString(1, tokenToRemove.authToken());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new ResponseException(500, e.toString());
        }
    }

    public AuthToken generate(String username) throws ResponseException {

        String tokenString = UUID.randomUUID().toString();

        try (var preparedStatement = conn.prepareStatement("INSERT INTO auth_tokens (token, username) VALUES(?, ?)")) {
            preparedStatement.setString(1, tokenString);
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new ResponseException(500, e.toString());
        }

        return new AuthToken(username, tokenString);
    }

    // just for testing!
    public ArrayList<AuthToken> listTokens() throws ResponseException {

        ArrayList<AuthToken> tokens = new ArrayList<>();

        try (var preparedStatement = conn.prepareStatement("SELECT token, username FROM auth_tokens")) {
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var token = rs.getString("token");
                    var username = rs.getString("username");

                    tokens.add(new AuthToken(username, token));
                }
            }
        } catch (SQLException e) {
            throw new ResponseException(500, e.toString());
        }

        return tokens;
    }

    public void clear() throws ResponseException {

        try (var createTableStatement = conn.prepareStatement("DELETE FROM auth_tokens")) {
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResponseException(500, e.toString());
        }
    }

    public void close() throws ResponseException {
        new Database().closeConnection(this.conn);
    }
}
