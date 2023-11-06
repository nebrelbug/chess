package passoffTests.serviceTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.AuthService;
import services.RegisterService;


public class RegisterServiceTest {

    static String username = "Ben Gubler";
    static String password = "asdfasdf";
    static String email = "ben@email.com";

    @Test
    public void registerPositiveTest() throws DataAccessException {

        AuthToken token = new RegisterService().register(username, password, email);

        Assertions.assertEquals(token.username(), username);

        new UserDAO().usernameExists(username);
    }

    @Test
    public void registerNegativeTest() {

        Assertions.assertThrows(DataAccessException.class, () -> {
            new RegisterService().register(username, password, email);
        });
    }

}
