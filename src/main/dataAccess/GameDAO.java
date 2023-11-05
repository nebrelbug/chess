package dataAccess;

import chess.BenChessGame;
import models.Game;

import java.util.ArrayList;

/**
 * Object to interface with games in the relational database
 */
public class GameDAO {


    static ArrayList<Game> games = new ArrayList<>();

    public static int create(String gameName) throws DataAccessException {

        int gameID = games.size() + 1;

        Game game = new Game(gameID, null, null, gameName, new BenChessGame());

        games.add(game);

        return gameID;
    }

    public static Game findById(int id) throws DataAccessException {
        for (Game game : games) {
            if (game.getID() == id) {
                return game;
            }
        }

        throw new DataAccessException(400, "Game ID not found");
    }

    public static void claimSpot(int gameID, String username, String color) throws DataAccessException {

        Game game = findById(gameID);

        if (color != null) game.claimSpot(username, color);
    }

    public static void updateGame(String gameID, String newGame) {

    }

    public static ArrayList<Game> listGames() {
        return games;
    }

    public static void clear() {
        games.clear();
    }
}
