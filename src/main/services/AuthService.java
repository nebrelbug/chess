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

    AuthDAO dao;

    public AuthService() throws DataAccessException {
        dao = new AuthDAO();
    }

    public AuthToken login(String username, String password) throws DataAccessException {

        if (username == null || password == null) throw new DataAccessException(400, "bad request");

        User user = UserDAO.getByUsername(username);

        if (user.password().equals(password)) {

            return dao.generate(username);
        }

        throw new DataAccessException(401, "unauthorized");
    }

    public void logout(String tokenString) throws DataAccessException {

        AuthToken token = dao.getByTokenString(tokenString);

        dao.remove(token);
    }
}
