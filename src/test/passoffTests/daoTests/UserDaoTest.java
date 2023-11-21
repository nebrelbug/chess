package passoffTests.daoTests;

import dataAccess.UserDAO;
import exceptions.ResponseException;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class UserDaoTest {

    UserDAO dao;

    public UserDaoTest() throws ResponseException {
        this.dao = new UserDAO();
    }

    @BeforeEach
    public void before() throws ResponseException {
        dao.clear();
    }

    @Test
    public void insertPositiveTest() throws ResponseException {
        dao.insert(new User("ben", "p", "e"));
        Assertions.assertTrue(dao.usernameExists("ben"));
    }

    @Test
    public void insertNegativeTest() {
        Assertions.assertThrows(ResponseException.class, () -> {
            dao.insert(null);
        });
    }

    @Test
    public void getByUsernamePositiveTest() throws ResponseException {
        dao.insert(new User("ben", "p", "e"));
        var user = dao.getByUsername("ben");
        Assertions.assertEquals("ben", user.username());
        Assertions.assertEquals("p", user.password());
        Assertions.assertEquals("e", user.email());
    }

    @Test
    public void getByUsernameNegativeTest() {
        Assertions.assertThrows(ResponseException.class, () -> {
            dao.getByUsername("unknownuser");
        });
    }

    @Test
    public void usernameExistsPositiveTest() throws ResponseException {
        dao.insert(new User("ben", "p", "e"));
        Assertions.assertTrue(dao.usernameExists("ben"));
    }

    @Test
    public void usernameExistsNegativeTest() {
        Assertions.assertFalse(dao.usernameExists("noname"));
    }

    @Test
    public void removePositiveTest() throws ResponseException {
        var u = new User("u1", "p", "e");
        dao.insert(u);
        Assertions.assertTrue(dao.usernameExists("u1"));
        dao.remove(u);
        Assertions.assertFalse(dao.usernameExists("u1"));
    }

    @Test
    public void removeNegativeTest() {
        Assertions.assertThrows(ResponseException.class, () -> {
            dao.remove(null);
        });
    }

    @Test
    public void clearTest() throws ResponseException {
        dao.insert(new User("u1", "p", "e"));
        dao.insert(new User("u2", "p", "e"));

        Assertions.assertTrue(dao.usernameExists("u1"));
        Assertions.assertTrue(dao.usernameExists("u2"));
        dao.clear();
        Assertions.assertFalse(dao.usernameExists("u1"));
        Assertions.assertFalse(dao.usernameExists("u2"));
    }

}

