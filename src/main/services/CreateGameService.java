package services;

import models.AuthToken;
import request.Request;
import response.Response;

/**
 * Service for game creation
 */
public class CreateGameService {

    /**
     * HTTP result of creating a game
     */
    public static class CreateGameResult extends Response {
        /**
         * game ID
         */
        final String gameID;

        /**
         * Instantiates a new CreateGameResult
         *
         * @param code   status code
         * @param gameID game ID
         */
        public CreateGameResult(int code, String gameID) {
            super(code);
            this.gameID = gameID;
        }
    }

    /**
     * HTTP Request to create a game
     */
    public static class CreateGameRequest extends Request {
        /**
         * AuthToken
         */
        final AuthToken authToken;
        /**
         * Match Name
         */
        final String gameName;

        /**
         * Instantiates a new CreateGameRequest
         *
         * @param authToken AuthToken
         * @param gameName  match name
         */
        public CreateGameRequest(AuthToken authToken, String gameName) {
            super(RequestMethod.POST, "/game");
            this.authToken = authToken;
            this.gameName = gameName;
        }
    }

    /**
     * Create a game
     *
     * @param request HTTP request to create the game
     * @return HTTP result of creating the game
     */
    public static CreateGameResult create(CreateGameRequest request) {
        return null;
    }

}