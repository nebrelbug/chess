package models;

import chess.BenChessGame;
import chess.ChessGame;

/**
 * Game model
 */

public record Game(int gameID, String whiteUsername, String blackUsername, Status status, String gameName,
                   BenChessGame game) {

    public enum Status {
        PLAYING,
        OVER
    }
}
