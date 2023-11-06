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

        if (playerColor != null) {
            if (!playerColor.equals("WHITE") && !playerColor.equals("BLACK") && !playerColor.isEmpty()) {
                throw new DataAccessException(400, "Invalid color");
            }
        }

        AuthToken token = new AuthDAO().getByTokenString(tokenString); // this will throw if invalid token

        new GameDAO().claimSpot(gameID, token.username(), playerColor);
    }
}
