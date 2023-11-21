package exceptions;

public class ResponseException extends Exception {
    public final int code;

    public ResponseException(int code, String message) {
        super(message);
        this.code = code;
    }
}