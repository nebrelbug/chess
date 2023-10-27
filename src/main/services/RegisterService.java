package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import models.AuthToken;
import models.User;

/**
 * Service to handle logging in
 */
public class RegisterService {

    public static AuthToken register(String username, String password, String email) throws DataAccessException {
        AuthToken newAuthToken = AuthDAO.generate(username);
        User newUser = new User(username, password, email);

        AuthDAO.insert(newAuthToken);
        UserDAO.insert(newUser);

        return newAuthToken;
    }

}
