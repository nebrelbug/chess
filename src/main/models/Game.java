package models;

import chess.ChessGame;

/**
 * Multiplayer game
 */
public class Game {
    /**
     * Game ID
     */
    final int gameID;
    /**
     * Username of player playing White
     */
    final String whiteUsername;
    /**
     * Username of player playing Black
     */
    final String blackUsername;
    /**
     * Match name
     */
    final String gameName;
    /**
     * The game itself
     */
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
    public Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }
}
