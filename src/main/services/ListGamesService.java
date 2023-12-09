package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import exceptions.ResponseException;
import models.Game;

import java.util.ArrayList;

/**
 * Service to list all games
 */
public class ListGamesService {

    public static ArrayList<Game> listGames(String tokenString) throws ResponseException {
        var dao = new AuthDAO();

        dao.getByTokenString(tokenString); // this will throw if invalid token

        dao.close();

        var gameDao = new GameDAO();
        var games = gameDao.listGames();
        gameDao.close();

        return games;
    }

}
