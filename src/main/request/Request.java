package request;

/**
 * HTTP Request
 */
public class Request {

    /**
     * Possible Request methods: GET, POST, PUT, DELETE, or OPTIONS
     */
    public static enum RequestMethod {
        GET,
        POST,
        PUT,
        DELETE,
        OPTIONS
    }

    /**
     * Method of the request
     */
    final RequestMethod method;

    /**
     * Path request will be sent to
     */
    final String path;


    /**
     * Instantiates a new Request.
     *
     * @param method method
     * @param path   path
     */
    public Request(RequestMethod method, String path) {
        this.method = method;
        this.path = path;
    }

}
