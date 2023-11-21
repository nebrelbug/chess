package passoffTests.daoTests;

import dataAccess.AuthDAO;
import exceptions.ResponseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.*;


public class AuthDaoTest {

    AuthDAO dao;

    public AuthDaoTest() throws ResponseException {
        this.dao = new AuthDAO();
    }

    @BeforeEach
    public void before() throws ResponseException {
        dao.clear();
    }

    @Test
    public void getByTokenStringPositiveTest() throws ResponseException {
        var token = dao.generate("myuser");
        var targetToken = dao.getByTokenString(token.authToken());
        Assertions.assertEquals(token.authToken(), targetToken.authToken());
    }

    @Test
    public void getByTokenStringNegativeTest() {
        Assertions.assertThrows(ResponseException.class, () -> {
            dao.getByTokenString("nonexistenttoken");
        });
    }

    @Test
    public void removePositiveTest() throws ResponseException {
        var token = dao.generate("user1");
        dao.remove(token);
        Assertions.assertEquals(0, dao.listTokens().size());
    }

    @Test
    public void removeNegativeTest() {
        Assertions.assertThrows(ResponseException.class, () -> {
            dao.remove(null);
        });
    }


    @Test
    public void generatePositiveTest() throws ResponseException {
        dao.generate("myuser");
        Assertions.assertEquals(1, dao.listTokens().size());
    }

    @Test
    public void generateNegativeTest() {
        Assertions.assertThrows(ResponseException.class, () -> {
            dao.generate(null);
        });
    }

    @Test
    public void clearTest() throws ResponseException {
        dao.generate("myuser");
        dao.generate("user2");
        dao.generate("user3");
        dao.generate("user4");

        Assertions.assertEquals(4, dao.listTokens().size());
        dao.clear();
        Assertions.assertEquals(0, dao.listTokens().size());
    }

}

