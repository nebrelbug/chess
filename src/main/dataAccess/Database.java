package dataAccess;

import exceptions.ResponseException;

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
    private static final String ROOT_URL = "jdbc:mysql://localhost:3306";


    /**
     * Gets a connection to the database.
     *
     * @return Connection the connection.
     * @throws exceptions.ResponseException if a data access error occurs.
     */
    public Connection getConnection() throws ResponseException {
        try {
            return DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    /**
     * Gets a connection to the database.
     *
     * @return Connection the connection.
     * @throws ResponseException if a data access error occurs.
     */
    public Connection getRootConnection() throws ResponseException {
        try {
            return DriverManager.getConnection(ROOT_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    /**
     * Closes the specified connection.
     *
     * @param connection the connection to be closed.
     * @throws ResponseException if a data access error occurs while closing the connection.
     */
    public void closeConnection(Connection connection) throws ResponseException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new ResponseException(500, e.getMessage());
            }
        }
    }

}