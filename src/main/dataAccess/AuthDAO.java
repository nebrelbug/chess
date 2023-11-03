package dataAccess;

import models.AuthToken;
import models.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Object to interface with AuthTokens in the relational database
 */
public class AuthDAO {

    static ArrayList<AuthToken> authTokens = new ArrayList<>();

    public static AuthToken getByTokenString(String tokenString) throws DataAccessException {
        for (AuthToken token : authTokens) {
            if (token.authToken().equals(tokenString)) {
                return token;
            }
        }

        throw new DataAccessException(401, "didn't find token string");
    }

    public static void remove(AuthToken tokenToRemove) throws DataAccessException {
        boolean authTokenRemoved = authTokens.remove(tokenToRemove);

        if (!authTokenRemoved) throw new DataAccessException(500, "AuthToken not found");
    }

    public static AuthToken generate(String username) {
        AuthToken newToken = new AuthToken(username, UUID.randomUUID().toString());

        authTokens.add(newToken);

        return newToken;
    }

    public static ArrayList<AuthToken> listTokens() {
        return authTokens;
    }

    public static void clear() {
        authTokens.clear();
    }
}
