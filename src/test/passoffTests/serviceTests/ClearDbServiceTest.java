package passoffTests.serviceTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
        ClearDbService.clear();

        ArrayList<AuthToken> authTokens = new AuthDAO().listTokens();

        Assertions.assertEquals(0, authTokens.size());
    }
}
