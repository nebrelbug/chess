package dataAccess;

import models.AuthToken;
import models.User;

/**
 * Object to interface with AuthTokens in the relational database
 */
public class AuthDAO {
    /**
     * Insert (add) a new AuthToken
     *
     * @param auth the AuthToken
     * @throws DataAccessException if the AuthToken already exists
     */
    public static void insert(AuthToken auth) throws DataAccessException {
    }

    /**
     * Gets the user linked to an AuthToken
     *
     * @param auth the AuthToken
     * @return linked user
     * @throws DataAccessException if AuthToken or user don't exist
     */
    public static User getUser(AuthToken auth) throws DataAccessException {
        return null;
    }

    /**
     * Gets respective users given an array of AuthTokens
     *
     * @param auth array of AuthToken objects
     * @return array of User objects
     * @throws DataAccessException if any AuthToken or user doesn't exist
     */
    public static User[] getUsers(AuthToken[] auth) throws DataAccessException {
        return null;
    }

    /**
     * Remove an AuthToken
     *
     * @param auth the AuthToken
     * @throws DataAccessException if no such AuthToken existed
     */
    public static void remove(AuthToken auth) throws DataAccessException {
    }

    /**
     * Clear all AuthToken objects from the database
     */
    public static void clear() {

    }
}
