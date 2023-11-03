package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import models.AuthToken;
import models.User;

/**
 * Service to register a new user
 */
public class RegisterService {

    public static AuthToken register(String username, String password, String email) throws DataAccessException {

        if (username == null || password == null || email == null) throw new DataAccessException(400, "bad request");

        if (UserDAO.usernameExists(username)) throw new DataAccessException(403, "already taken");

        User newUser = new User(username, password, email);
        UserDAO.insert(newUser);

        return AuthDAO.generate(username);
    }

}
