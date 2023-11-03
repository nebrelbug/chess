package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.AuthToken;

/**
 * Service to handle joining a game
 */
public class JoinGameService {

    public static void join(String tokenString, int gameID, String playerColor) throws DataAccessException {

        AuthToken token = AuthDAO.getByTokenString(tokenString); // this will throw if invalid token

        GameDAO.claimSpot(gameID, token.username(), playerColor);
    }
}
