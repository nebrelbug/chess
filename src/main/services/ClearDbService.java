package services;

import dataAccess.*;
import exceptions.ResponseException;

/**
 * Service for clearing the database
 */
public class ClearDbService {

    public static void clear() throws ResponseException {
        new AuthDAO().clear();
        new GameDAO().clear();
        new UserDAO().clear();
    }
}