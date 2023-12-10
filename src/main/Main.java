import exceptions.ResponseException;

public class Main {
    public static void main(String[] args) throws ResponseException {
        new DatabaseInit().initialize();
    }
}
