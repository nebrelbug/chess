package passoffTests.serviceTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.AuthService;
import services.ClearDbService;
import services.RegisterService;

import java.util.ArrayList;


public class ClearDbServiceTest {

    @BeforeAll
    public static void before() throws DataAccessException {
        ClearDbService.clear();

        var rService = new RegisterService();
        rService.register("HGEIOHI", "HOJPPJO", "EGOIH");
        rService.register("asfdjl", "afsjkl", "asdfjkl");
    }

    @Test
    public void clearTest() throws DataAccessException {

        ArrayList<AuthToken> authTokens = new AuthDAO().listTokens();

        ClearDbService.clear();

        Assertions.assertEquals(0, authTokens.size());
    }
}
