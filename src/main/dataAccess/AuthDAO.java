package dataAccess;

import models.AuthToken;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Object to interface with AuthTokens in the relational database
 */
public class AuthDAO {

    static ArrayList<AuthToken> authTokens = new ArrayList<>();

    public static void insert(AuthToken token) throws DataAccessException {
        authTokens.add(token);
    }

    public static void remove(AuthToken tokenToRemove) throws DataAccessException {
        boolean authTokenRemoved = authTokens.remove(tokenToRemove);

        if (!authTokenRemoved) throw new DataAccessException("AuthToken not found");
    }

    public static AuthToken generate(String username) {
        return new AuthToken(UUID.randomUUID().toString(), username);
    }

    public static void clear() {
        authTokens.clear();
    }
}
