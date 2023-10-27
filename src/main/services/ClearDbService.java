package services;

import request.Request;
import response.Response;

/**
 * Service for clearing the database
 */
public class ClearDbService {

    /**
     * HTTP Result after the database has been cleared
     */
    public static class clearDbResult extends Response {
        /**
         * Instantiates a new clearDbResult
         *
         * @param code status code
         */
        public clearDbResult(int code) {
            super(code);
        }
    }

    /**
     * HTTP Request to clear the database
     */
    public static class clearDbRequest extends Request {
        /**
         * Instantiates a new clearDbRequest
         */
        public clearDbRequest() {
            super(RequestMethod.DELETE, "/db");
        }
    }

    /**
     * Clear the database
     *
     * @param request database clear request
     * @return HTTP result of database clearing
     */
    public static clearDbResult clear(clearDbRequest request) {
        return null;
    }

}