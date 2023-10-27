package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import models.AuthToken;
import models.User;

/**
 * Service to handle logging in
 */
public class AuthService {

    public static AuthToken login(String username, String password) throws DataAccessException {

        User user = UserDAO.getByUsername(username);

        if (user.getPassword().equals(password)) {
            AuthToken newAuthToken = AuthDAO.generate(username);

            return newAuthToken;
        }

        throw new DataAccessException("Error: unauthorized");
    }

    public static void logout(AuthToken auth) throws DataAccessException {
        AuthDAO.remove(auth);
    }
}
