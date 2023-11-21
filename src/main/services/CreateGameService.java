package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;

/**
 * Service to create a game
 */
public class CreateGameService {

    public static int create(String tokenString, String gameName) throws DataAccessException {

        new AuthDAO().getByTokenString(tokenString); // this will throw if invalid token

        return new GameDAO().create(gameName);
    }

}
