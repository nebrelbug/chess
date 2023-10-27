package dataAccess;

import models.Game;

import java.util.ArrayList;

/**
 * Object to interface with games in the relational database
 */
public class GameDAO {

    static ArrayList<Game> games = new ArrayList<>();

    public static void insert(Game game) throws DataAccessException {
        games.add(game);
    }

    public static Game findById(String id) throws DataAccessException {
        for (Game game : games) {
            if (game.getID().equals(id)) {
                return game;
            }
        }

        throw new DataAccessException("Game ID not found");
    }

    public static Game[] findByIds(String[] ids) throws DataAccessException {

        Game[] res = new Game[ids.length];

        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            res[i] = findById(id);
        }

        return res;
    }

    public static void claimSpot(String gameID, String username) throws DataAccessException {

        Game game = findById(gameID);

        game.claimSpot(username);

    }

    public static void updateGame(String gameID, String newGame) {

    }

    public static void remove(String gameID) throws DataAccessException {
        Game game = findById(gameID);

        boolean gameRemoved = games.remove(game);

        if (!gameRemoved) throw new DataAccessException("Game ID not found");
    }

    public static void clear() {
        games.clear();
    }
}
