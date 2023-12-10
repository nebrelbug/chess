package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }


    private final CommandType commandType;
    private final String authToken;
    private final int gameID;
    private final ChessGame.TeamColor playerColor;
    private final ChessMove move;


    public UserGameCommand(String authToken, CommandType commandType, int gameID, ChessGame.TeamColor playerColor, ChessMove move) {
        this.authToken = authToken;
        this.commandType = commandType;
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.move = move;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    public String getAuthString() {
        return authToken;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand that))
            return false;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}
