package services;

import dataAccess.*;
import exceptions.ResponseException;

/**
 * Service for clearing the database
 */
public class ClearDbService {

    public static void clear() throws ResponseException {
        var authDao = new AuthDAO();
        var gameDao = new GameDAO();
        var userDao = new UserDAO();

        authDao.clear();
        gameDao.clear();
        userDao.clear();

        authDao.close();
        gameDao.close();
        userDao.close();
    }
}