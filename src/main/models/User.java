package models;

/**
 * User of the chess application
 */
public class User {
    /**
     * Player username
     */
    final String username;
    /**
     * Player password
     */
    final String password;
    /**
     * Player email
     */
    final String email;

    /**
     * Instantiates a new User.
     *
     * @param username username
     * @param password password
     * @param email    email
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
