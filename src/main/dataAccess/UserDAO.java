package dataAccess;

import models.AuthToken;
import models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class UserDAO {

    Connection conn;

    public UserDAO() throws DataAccessException {
        this.conn = new Database().getConnection();
    }

    public void insert(User user) throws DataAccessException {

        try (var preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES(?, ?, ?)")) {
            preparedStatement.setString(1, user.username());
            preparedStatement.setString(2, user.password());
            preparedStatement.setString(3, user.email());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new DataAccessException(500, e.toString());
        }
    }

    public User getByUsername(String targetUsername) throws DataAccessException {

        try (var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM users WHERE username=?")) {

            preparedStatement.setString(1, targetUsername);

            try (var rs = preparedStatement.executeQuery()) {
                if (!rs.next()) throw new DataAccessException(401, "User not found");

                var username = rs.getString("username");
                var password = rs.getString("password");
                var email = rs.getString("email");

                return new User(username, password, email);
            }
        } catch (SQLException e) {
            throw new DataAccessException(500, e.toString());
        }
    }

    public boolean usernameExists(String username) {

        var userExists = true;

        try {
            getByUsername(username);
        } catch (Exception e) {
            userExists = false;
        }

        return userExists;
    }

    public void remove(User user) throws DataAccessException {
        try (var preparedStatement = conn.prepareStatement("DELETE FROM users WHERE username=?")) {
            preparedStatement.setString(1, user.username());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new DataAccessException(500, "System error: failed to delete user");
        }
    }

    public void clear() throws DataAccessException {
        try (var createTableStatement = conn.prepareStatement("DELETE FROM users")) {
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(500, "System error: failed to clear database");
        }
    }
}
