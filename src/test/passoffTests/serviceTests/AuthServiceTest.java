package passoffTests.serviceTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.*;
import services.AuthService;
import services.ClearDbService;
import services.RegisterService;


public class AuthServiceTest {

    static String username = "GEHIO";
    static String password = "aklsdfjasdf";
    static String email = "hsdhkgsdhl@gmaisl.com";

    static AuthToken auth;

    @BeforeAll
    public static void before() throws DataAccessException {
        ClearDbService.clear();
        auth = new RegisterService().register(username, password, email);
    }

    @Test
    public void loginPositiveTest() throws DataAccessException {

        AuthToken token = new AuthService().login(username, password);

        Assertions.assertEquals(token.username(), username);
    }

    @Test
    public void loginNegativeTest() {

        Assertions.assertThrows(DataAccessException.class, () -> {
            new AuthService().login(username, "wrong");
        });
    }

    @Test
    public void logoutPositiveTest() throws DataAccessException {

        new AuthService().logout(auth.authToken());

        Assertions.assertThrows(DataAccessException.class, () -> {
            new AuthDAO().getByTokenString(auth.authToken());
        });
    }


    @Test
    public void logoutNegativeTest() {

        Assertions.assertThrows(DataAccessException.class, () -> {
            new AuthService().logout("Nonexistent token");
        });

    }
}
