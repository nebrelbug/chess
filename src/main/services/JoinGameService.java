package services;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import exceptions.ResponseException;
import models.AuthToken;

/**
 * Service to handle joining a game
 */
public class JoinGameService {

    public static void join(String tokenString, int gameID, String playerColor) throws ResponseException {

        if (playerColor != null) {
            if (!playerColor.equals("WHITE") && !playerColor.equals("BLACK") && !playerColor.isEmpty()) {
                throw new ResponseException(400, "Invalid color");
            }
        }

        var authDao = new AuthDAO();
        AuthToken token = authDao.getByTokenString(tokenString); // this will throw if invalid token
        authDao.close();

        var dao = new GameDAO();
        dao.claimSpot(gameID, token.username(), playerColor);
        dao.close();
    }
}
