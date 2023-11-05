package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Game;

import java.util.ArrayList;

/**
 * Service to list all games
 */
public class ListGamesService {

    public static ArrayList<Game> listGames(String tokenString) throws DataAccessException {
        new AuthDAO().getByTokenString(tokenString); // this will throw if invalid token

        return GameDAO.listGames();
    }

}
