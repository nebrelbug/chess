package models;

/**
 * Authentication Token
 */
public class AuthToken {
    /**
     * The Auth token itself
     */
    final String authToken;
    /**
     * Username associated with the Auth token
     */
    final String username;

    /**
     * Instantiates a new Auth token.
     *
     * @param authToken the auth token
     * @param username  the username
     */
    public AuthToken(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }
}
