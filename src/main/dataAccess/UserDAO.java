package dataAccess;

import models.User;

/**
 * The type User dao.
 */
public class UserDAO {
    /**
     * Insert (add) a new user
     *
     * @param user User object to add
     * @throws DataAccessException if the user already exists
     */
    public static void insert(User user) throws DataAccessException {
    }

    /**
     * Gets the User object associated with a username
     *
     * @param username username
     * @return User object linked with the username
     * @throws DataAccessException if the username is invalid
     */
    public static User getByUsername(String username) throws DataAccessException {
        return null;
    }

    /**
     * Gets the User objects associated with an array of usernames
     *
     * @param usernames array of usernames
     * @return array of User objects
     * @throws DataAccessException if any username is invalid
     */
    public static User[] getByUsernames(String[] usernames) throws DataAccessException {
        return null;
    }

    /**
     * Remove a user from the database
     *
     * @param user User object
     * @throws DataAccessException if the user doesn't exist
     */
    public static void remove(User user) throws DataAccessException {
    }

    /**
     * Clear all users from the database
     */
    public static void clear() {

    }
}
