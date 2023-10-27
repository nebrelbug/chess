package services;

import request.Request;
import response.Response;

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