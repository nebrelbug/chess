package services;

import request.Request;
import response.Response;

/**
 * Service to handle user registration
 */
public class RegisterService {

    /**
     * HTTP result of registering
     */
    public static class RegisterResult extends Response {
        /**
         * Instantiates a new RegisterResult
         *
         * @param code HTTP status code
         */
        public RegisterResult(int code) {
            super(code);
        }
    }

    /**
     * HTTP request to register a new user
     */
    public static class RegisterRequest extends Request {
        /**
         * username
         */
        final String username;
        /**
         * password
         */
        final String password;
        /**
         * email
         */
        final String email;

        /**
         * Instantiates a new RegisterRequest
         *
         * @param username username
         * @param password password
         * @param email    email
         */
        public RegisterRequest(String username, String password, String email) {
            super(RequestMethod.POST, "/user");
            this.username = username;
            this.password = password;
            this.email = email;
        }
    }

    /**
     * Register a new user
     *
     * @param request HTTP request to register a new user
     * @return HTTP result of registering
     */
    public static RegisterResult register(RegisterRequest request) {
        return null;
    }
}
