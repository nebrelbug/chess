package services;

import models.AuthToken;
import request.Request;
import response.Response;

/**
 * Service for joining a game
 */
public class JoinGameService {

    /**
     * HTTP result of joining the game
     */
    public static class JoinGameResult extends Response {
        /**
         * Instantiates a new JoinGameResult
         *
         * @param code status code
         */
        public JoinGameResult(int code) {
            super(code);
        }
    }

    /**
     * HTTP request to join game
     */
    public static class JoinGameRequest extends Request {
        /**
         * AuthToken
         */
        final AuthToken authToken;
        /**
         * player color
         */
        final String playerColor;
        /**
         * game ID
         */
        final String gameID;

        /**
         * Instantiates a new JoinGameRequest
         *
         * @param authToken   AuthToken
         * @param playerColor player color
         * @param gameID      game ID
         */
        public JoinGameRequest(AuthToken authToken, String playerColor, String gameID) {
            super(RequestMethod.PUT, "/game");
            this.authToken = authToken;
            this.playerColor = playerColor;
            this.gameID = gameID;
        }
    }

    /**
     * Join a game
     *
     * @param request HTTP request to join game
     * @return HTTP result of joining game
     */
    public static JoinGameResult join(JoinGameRequest request) {
        return null;
    }

}