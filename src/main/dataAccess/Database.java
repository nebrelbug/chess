package dataAccess;

import models.AuthToken;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Responsible for creating connections to the database.
 */
public class Database {

    public static final String DB_NAME = "chess";
    private static final String DB_USERNAME = "admin";
    private static final String DB_PASSWORD = "password";

    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;

    /**
     * Gets a connection to the database.
     *
     * @return Connection the connection.
     * @throws DataAccessException if a data access error occurs.
     */
    public Connection getConnection() throws DataAccessException {
        try {
            return DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            throw new DataAccessException(500, e.getMessage());
        }
    }
    
}