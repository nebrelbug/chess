package services;

import dataAccess.AuthDAO;
import exceptions.ResponseException;
import dataAccess.UserDAO;
import models.AuthToken;
import models.User;

/**
 * Service to handle logging in and logging out
 */
public class AuthService {

    AuthDAO authDao;
    UserDAO userDAO;

    public AuthService() throws ResponseException {
        authDao = new AuthDAO();
        userDAO = new UserDAO();
    }
    
    public AuthToken login(String username, String password) throws ResponseException {

        if (username == null || password == null) throw new ResponseException(400, "bad request");

        User user = userDAO.getByUsername(username);

        if (user.password().equals(password)) {
            return authDao.generate(username);
        }

        throw new ResponseException(401, "unauthorized");
    }

    public void logout(String tokenString) throws ResponseException {

        AuthToken token = authDao.getByTokenString(tokenString);

        authDao.remove(token);
    }
}
