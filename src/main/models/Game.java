package models;

import chess.ChessGame;
import dataAccess.DataAccessException;

/**
 * Multiplayer game
 */
public class Game {

    final String gameID;
    String whiteUsername;
    String blackUsername;
    final String gameName;
    final ChessGame game;

    /**
     * Instantiates a new Game.
     *
     * @param gameID        game ID
     * @param whiteUsername white username
     * @param blackUsername black username
     * @param gameName      match name
     * @param game          game state
     */
    public Game(String gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public String getID() {
        return gameID;
    }

    public void claimSpot(String username) throws DataAccessException {
        if (whiteUsername == null) {
            whiteUsername = username;
        } else if (blackUsername == null) {
            blackUsername = username;
        } else {
            throw new DataAccessException("No spots available");
        }
    }
}
