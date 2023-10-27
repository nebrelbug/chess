package models;

/**
 * User of the chess application
 */
public class User {

    final String username;
    final String password;
    final String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
