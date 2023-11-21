import dataAccess.DataAccessException;
import dataAccess.Database;
import exceptions.ResponseException;

import java.sql.SQLException;

public class DatabaseInit {

    Database db;

    public DatabaseInit() {
        this.db = new Database();
    }

    void configureDatabase() throws ResponseException {
        try (var conn = db.getRootConnection()) {
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chess");
            createDbStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResponseException(500, e.toString());
        }
    }

    void configureGamesTable() throws ResponseException {
        try (var conn = db.getConnection()) {

            conn.setCatalog("chess");

            var createGamesTable = """
                    CREATE TABLE IF NOT EXISTS games (
                        id INT NOT NULL AUTO_INCREMENT,
                        white_username VARCHAR(255),
                        black_username VARCHAR(255),
                        name VARCHAR(255) NOT NULL,
                        board_state longtext NOT NULL,
                        PRIMARY KEY (id)
                    )""";


            try (var createTableStatement = conn.prepareStatement(createGamesTable)) {
                createTableStatement.executeUpdate();
            } catch (SQLException e) {
                throw new ResponseException(500, e.toString());
            }
        } catch (SQLException e) {
            throw new ResponseException(500, e.toString());
        }
    }

    void configureAuthTokensTable() throws ResponseException {
        try (var conn = db.getConnection()) {

            conn.setCatalog("chess");

            var createAuthTokensTable = """
                    CREATE TABLE IF NOT EXISTS auth_tokens (
                        id INT NOT NULL AUTO_INCREMENT,
                        token VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
                    )""";


            try (var createTableStatement = conn.prepareStatement(createAuthTokensTable)) {
                createTableStatement.executeUpdate();
            } catch (SQLException e) {
                throw new ResponseException(500, e.toString());
            }
        } catch (SQLException e) {
            throw new ResponseException(500, e.toString());
        }
    }

    void configureUsersTable() throws ResponseException {
        try (var conn = db.getConnection()) {

            conn.setCatalog("chess");

            var createUsersTable = """
                    CREATE TABLE IF NOT EXISTS users (
                        id INT NOT NULL AUTO_INCREMENT,
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
                    )""";


            try (var createTableStatement = conn.prepareStatement(createUsersTable)) {
                createTableStatement.executeUpdate();
            } catch (SQLException e) {
                throw new ResponseException(500, e.toString());
            }
        } catch (SQLException e) {
            throw new ResponseException(500, e.toString());
        }
    }

    public void initialize() throws ResponseException {
        configureDatabase();
        configureGamesTable();
        configureAuthTokensTable();
        configureUsersTable();
    }
}
