package dataAccess;

import models.Game;

/**
 * Object to interface with games in the relational database
 */
public class GameDAO {
    /**
     * Insert (add) a new game
     *
     * @param game game to add
     * @throws DataAccessException if the game already exists
     */
    public static void insert(Game game) throws DataAccessException {
    }

    /**
     * Find game by ID
     *
     * @param id game ID
     * @return game model
     * @throws DataAccessException if the game doesn't exist
     */
    public static Game findById(String id) throws DataAccessException {
        return null;
    }

    /**
     * Find game using a list of IDs
     *
     * @param ids array of game IDs
     * @return an array of game models
     * @throws DataAccessException if any game doesn't exist
     */
    public static Game[] findByIds(String[] ids) throws DataAccessException {
        return null;
    }

    /**
     * Claim a user's spot in a game.
     *
     * @param gameID   game ID
     * @param username username
     * @throws DataAccessException if game ID or username don't exist
     */
    public static void claimSpot(String gameID, String username) throws DataAccessException {
    }

    /**
     * Update game
     *
     * @param gameID  game ID
     * @param newGame string with new game state
     */
    public static void updateGame(String gameID, String newGame) {

    }

    /**
     * Remove a game from the database
     *
     * @param gameID game ID
     * @throws DataAccessException if game ID doesn't exist
     */
    public static void remove(String gameID) throws DataAccessException {
    }

    /**
     * Clear all games from the relational database
     */
    public static void clear() {

    }
}
