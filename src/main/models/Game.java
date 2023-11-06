package models;

import chess.ChessGame;

/**
 * Game model
 */

public record Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
}
