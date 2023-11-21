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
        new AuthDAO().getByTokenString(tokenString); // this will throw if invalid token

        return new GameDAO().listGames();
    }

}
