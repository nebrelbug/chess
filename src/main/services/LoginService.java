package services;

import models.AuthToken;
import request.Request;
import response.Response;

/**
 * Service to handle logging in
 */
public class LoginService {

    /**
     * HTTP result of logging in
     */
    public static class LoginResult extends Response {
        /**
         * Instantiates a new LoginResult
         *
         * @param code HTTP status code
         */
        public LoginResult(int code) {
            super(code);
        }
    }

    /**
     * HTTP result of logging out
     */
    public static class LogoutResult extends Response {
        /**
         * Instantiates a new LogoutResult
         *
         * @param code HTTP status code
         */
        public LogoutResult(int code) {
            super(code);
        }
    }

    /**
     * HTTP request to log in
     */
    public static class LoginRequest extends Request {
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
         * Instantiates a new LoginRequest
         *
         * @param username username
         * @param password password
         * @param email    email
         */
        public LoginRequest(String username, String password, String email) {
            super(RequestMethod.POST, "/session");
            this.username = username;
            this.password = password;
            this.email = email;
        }
    }

    /**
     * HTTP request to log out
     */
    public static class LogoutRequest extends Request {
        /**
         * AuthToken
         */
        final AuthToken authToken;

        /**
         * Instantiates a new LogoutRequest
         *
         * @param authToken AuthToken
         */
        public LogoutRequest(AuthToken authToken) {
            super(RequestMethod.DELETE, "/session");
            this.authToken = authToken;
        }
    }

    /**
     * Login
     *
     * @param request HTTP request to log in
     * @return HTTP result with AuthToken
     */
    public static LoginResult login(LoginRequest request) {
        return null;
    }

    /**
     * Logout
     *
     * @param request HTTP request to log out
     * @return HTTP result of logging out
     */
    public static LogoutResult logout(LogoutRequest request) {
        return null;
    }

}
