package request;

/**
 * Indicates that the request failed
 */
public class RequestException extends Exception {

    public RequestException(String message) {
        super(message);
    }
}