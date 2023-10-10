package chess;

import java.util.Collection;

public class BenChessGame implements ChessGame {

    private ChessGame.TeamColor teamTurn;
    private ChessBoard board;

    public BenChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.board = new BenChessBoard();
    }

    @Override
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        return null;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {

    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        return false;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        return false;
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    @Override
    public ChessBoard getBoard() {
        return this.board;
    }
}
