package services;

import models.AuthToken;
import request.Request;
import response.Response;

/**
 * Service to list all games
 */
public class ListGamesService {

    /**
     * Representation of a game
     */
    public static class ListedGame {
        /**
         * game ID
         */
        final String gameID;
        /**
         * username that plays White
         */
        final String whiteUsername;
        /**
         * username that plays Black
         */
        final String blackUsername;
        /**
         * match name
         */
        final String gameName;

        /**
         * Instantiates a new ListedGame
         *
         * @param gameID        game ID
         * @param whiteUsername white username
         * @param blackUsername black username
         * @param gameName      match name
         */
        public ListedGame(String gameID, String whiteUsername, String blackUsername, String gameName) {
            this.gameID = gameID;
            this.whiteUsername = whiteUsername;
            this.blackUsername = blackUsername;
            this.gameName = gameName;
        }
    }

    /**
     * HTTP result of listing all database games
     */
    public static class ListGamesResult extends Response {
        /**
         * List of games
         */
        final ListedGame[] games;

        /**
         * Instantiates a new ListGamesResult.
         *
         * @param code  HTTP status code
         * @param games list of games
         */
        public ListGamesResult(int code, ListedGame[] games) {
            super(code);
            this.games = games;
        }
    }

    /**
     * HTTP request to list games
     */
    public static class ListGamesRequest extends Request {
        /**
         * AuthToken
         */
        final AuthToken authToken;

        /**
         * Instantiates a new ListGamesRequest
         *
         * @param authToken AuthToken
         */
        public ListGamesRequest(AuthToken authToken) {
            super(RequestMethod.GET, "/game");
            this.authToken = authToken;
        }
    }

    /**
     * List current games
     *
     * @param request HTTP request to list games
     * @return HTTP response with list of games
     */
    public static ListGamesResult list(ListGamesRequest request) {
        return null;
    }

}