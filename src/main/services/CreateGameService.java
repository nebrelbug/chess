package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import models.AuthToken;
import models.User;

/**
 * Service to create a game
 */
public class CreateGameService {

    public static int create(String tokenString, String gameName) throws DataAccessException {

        new AuthDAO().getByTokenString(tokenString); // this will throw if invalid token

        return GameDAO.create(gameName);
    }

}
