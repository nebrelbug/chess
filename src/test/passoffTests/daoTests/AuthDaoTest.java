package passoffTests.daoTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.*;


public class AuthDaoTest {

    AuthDAO dao;

    public AuthDaoTest() throws DataAccessException {
        this.dao = new AuthDAO();
    }

    @BeforeEach
    public void before() throws DataAccessException {
        dao.clear();
    }

    @Test
    public void getByTokenStringPositiveTest() throws DataAccessException {
        var token = dao.generate("myuser");
        var targetToken = dao.getByTokenString(token.authToken());
        Assertions.assertEquals(token.authToken(), targetToken.authToken());
    }

    @Test
    public void getByTokenStringNegativeTest() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            dao.getByTokenString("nonexistenttoken");
        });
    }

    @Test
    public void removePositiveTest() throws DataAccessException {
        var token = dao.generate("user1");
        dao.remove(token);
        Assertions.assertEquals(0, dao.listTokens().size());
    }

    @Test
    public void removeNegativeTest() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            dao.remove(null);
        });
    }


    @Test
    public void generatePositiveTest() throws DataAccessException {
        dao.generate("myuser");
        Assertions.assertEquals(1, dao.listTokens().size());
    }

    @Test
    public void generateNegativeTest() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            dao.generate(null);
        });
    }

    @Test
    public void clearTest() throws DataAccessException {
        dao.generate("myuser");
        dao.generate("user2");
        dao.generate("user3");
        dao.generate("user4");

        Assertions.assertEquals(4, dao.listTokens().size());
        dao.clear();
        Assertions.assertEquals(0, dao.listTokens().size());
    }

}

