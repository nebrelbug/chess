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

    AuthDAO authDao;
    UserDAO userDAO;

    public RegisterService() throws DataAccessException {
        authDao = new AuthDAO();
        userDAO = new UserDAO();
    }

    public AuthToken register(String username, String password, String email) throws DataAccessException {

        if (username == null || password == null || email == null) throw new DataAccessException(400, "bad request");

        if (userDAO.usernameExists(username)) throw new DataAccessException(403, "already taken");
        User newUser = new User(username, password, email);
        userDAO.insert(newUser);

        return authDao.generate(username);
    }

}
