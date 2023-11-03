package services;

import dataAccess.*;

/**
 * Service for clearing the database
 */
public class ClearDbService {

    public static void clear() {
        AuthDAO.clear();
        GameDAO.clear();
        UserDAO.clear();
    }
}