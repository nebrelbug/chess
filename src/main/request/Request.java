package request;

/**
 * HTTP Request
 */
public class Request {

    public final String username;
    public final String password;
    public final String email;

    public Request(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

}
