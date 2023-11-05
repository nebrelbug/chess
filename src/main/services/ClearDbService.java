package services;

import dataAccess.*;

/**
 * Service for clearing the database
 */
public class ClearDbService {

    public static void clear() throws DataAccessException {
        new AuthDAO().clear();
        GameDAO.clear();
        UserDAO.clear();
    }
}