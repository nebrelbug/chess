package dataAccess;

import models.User;

import java.util.ArrayList;
import java.util.Arrays;

public class UserDAO {

    static ArrayList<User> users = new ArrayList<>();

    public static void insert(User user) throws DataAccessException {
        users.add(user);
    }

    public static User getByUsername(String username) throws DataAccessException {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        throw new DataAccessException("Username not found");
    }

    public static User[] getByUsernames(String[] usernames) throws DataAccessException {
        User[] res = new User[usernames.length];

        for (int i = 0; i < usernames.length; i++) {
            String username = usernames[i];
            res[i] = getByUsername(username);
        }

        return res;
    }

    public static void remove(User user) throws DataAccessException {

        boolean userRemoved = users.remove(user);

        if (!userRemoved) throw new DataAccessException("Game ID not found");
    }

    public static void clear() {
        users.clear();
    }
}
