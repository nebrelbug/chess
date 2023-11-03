package dataAccess;

import models.User;

import java.util.ArrayList;

public class UserDAO {

    static ArrayList<User> users = new ArrayList<>();

    public static void insert(User user) throws DataAccessException {
        users.add(user);
    }

    public static User getByUsername(String username) throws DataAccessException {
        for (User user : users) {
            if (user.username().equals(username)) {
                return user;
            }
        }

        throw new DataAccessException(401, "unauthorized");
    }

    public static boolean usernameExists(String username) {
        for (User user : users) {
            if (user.username().equals(username)) {
                return true;
            }
        }
        return false;
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

        if (!userRemoved) throw new DataAccessException(500, "User not found");
    }

    public static void clear() {
        users.clear();
    }
}
