package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import exceptions.ResponseException;

/**
 * Service to create a game
 */
public class CreateGameService {

    public static int create(String tokenString, String gameName) throws ResponseException {

        var dao = new AuthDAO();

        dao.getByTokenString(tokenString); // this will throw if invalid token

        dao.close();

        var gameDao = new GameDAO();
        var game = gameDao.create(gameName);
        gameDao.close();

        return game;
    }

}
