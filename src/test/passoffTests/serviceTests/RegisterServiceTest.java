package passoffTests.serviceTests;

import dataAccess.UserDAO;
import exceptions.ResponseException;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import services.RegisterService;


public class RegisterServiceTest {

    static String username = "Ben Gubler";
    static String password = "asdfasdf";
    static String email = "ben@email.com";

    @Test
    public void registerPositiveTest() throws ResponseException {

        AuthToken token = new RegisterService().register(username, password, email);

        Assertions.assertEquals(token.username(), username);

        var dao = new UserDAO();
        dao.usernameExists(username);
        dao.close();
    }

    @Test
    public void registerNegativeTest() {

        Assertions.assertThrows(ResponseException.class, () -> {
            new RegisterService().register(username, password, email);
        });
    }

}
