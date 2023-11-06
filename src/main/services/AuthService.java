package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import models.AuthToken;
import models.User;

/**
 * Service to handle logging in and logging out
 */
public class AuthService {

    AuthDAO authDao;
    UserDAO userDAO;

    public AuthService() throws DataAccessException {
        authDao = new AuthDAO();
        userDAO = new UserDAO();
    }

    // TODO: delete auth tokens after logging in or out

    public AuthToken login(String username, String password) throws DataAccessException {

        if (username == null || password == null) throw new DataAccessException(400, "bad request");

        User user = userDAO.getByUsername(username);

        if (user.password().equals(password)) {
            return authDao.generate(username);
        }

        throw new DataAccessException(401, "unauthorized");
    }

    public void logout(String tokenString) throws DataAccessException {

        AuthToken token = authDao.getByTokenString(tokenString);

        authDao.remove(token);
    }
}
