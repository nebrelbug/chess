package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.WHITE;

public class BenChessGame implements ChessGame {

    private TeamColor teamTurn;
    private BenChessBoard board;

    public BenChessGame() {
        this.teamTurn = WHITE;
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

    public void initialize() {
        this.board.resetBoard();
    }

    private Collection<ChessMove> _validMoves(ChessPosition startPosition, ChessBoard testBoard) {
        ChessPiece pieceThere = testBoard.getPiece(startPosition);

        return pieceThere.pieceMoves(testBoard, startPosition);
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        Collection<ChessMove> movesOne = _validMoves(startPosition, this.board);

        ArrayList<ChessMove> res = new ArrayList<>();

        for (ChessMove move : movesOne) {
            ChessPiece pieceToMove = this.board.getPiece(startPosition);
            TeamColor colorMoving = pieceToMove.getTeamColor();
            if (!willCauseCheck(move, colorMoving)) {
                res.add(move);
            }
        }

        return res;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece pieceToMove = this.board.getPiece(startPosition);

        if (pieceToMove == null) {
            throw new InvalidMoveException("You don't have a piece there!");
        }

        TeamColor colorMoving = pieceToMove.getTeamColor();
        ChessPiece.PieceType promotion = move.getPromotionPiece();

        // make move out of turn
        if (colorMoving != teamTurn) throw new InvalidMoveException("It's not your turn!");

        Collection<ChessMove> validMoves = this.validMoves(startPosition);

        var testMove = new BenChessMove(move.getStartPosition(), move.getEndPosition(), promotion);
        // so that validMoves.contains works regardless of the promotion piece

        // impossible move
        if (!validMoves.contains(testMove)) throw new InvalidMoveException("Move impossible for piece type.");

        // move into check
        if (willCauseCheck(move, colorMoving)) throw new InvalidMoveException("Can't move king into check.");

        ChessPiece newPiece = promotion == null ? pieceToMove : new BenChessPiece(colorMoving, promotion);

        this.board.addPiece(startPosition, null);
        this.board.addPiece(endPosition, newPiece);

        this.teamTurn = this.teamTurn == WHITE ? TeamColor.BLACK : WHITE;
    }

    private ChessPosition getKingPosition(TeamColor kingColor, ChessBoard board) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition testPosition = new BenChessPosition(row, col);
                ChessPiece pieceThere = board.getPiece(testPosition);
                if (pieceThere != null && pieceThere.getPieceType() == ChessPiece.PieceType.KING && pieceThere.getTeamColor() == kingColor) {
                    return testPosition;
                }
            }
        }

        return null;
    }

    private boolean positionThreatened(ChessPosition position, TeamColor teamColor, ChessBoard testBoard) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition testPosition = new BenChessPosition(row, col);
                ChessPiece threateningPiece = testBoard.getPiece(testPosition);

                if (threateningPiece != null && threateningPiece.getTeamColor() != teamColor) {

                    Collection<ChessMove> potentialMoves = _validMoves(testPosition, testBoard);

                    for (ChessMove move : potentialMoves) {
                        ChessPosition endPosition = move.getEndPosition();
                        if (endPosition.equals(position)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    @Override
    public boolean isInCheck(TeamColor teamColor) {

        ChessPosition kingPosition = getKingPosition(teamColor, this.board);

        return positionThreatened(kingPosition, teamColor, this.board);
    }

    private boolean willCauseCheck(ChessMove move, TeamColor teamColor) {
        ChessBoard clonedBoard = board.returnClone();
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece pieceToMove = clonedBoard.getPiece(startPosition);

        clonedBoard.addPiece(startPosition, null);
        clonedBoard.addPiece(endPosition, pieceToMove);

        ChessPosition kingPosition = getKingPosition(teamColor, clonedBoard);

        return positionThreatened(kingPosition, teamColor, clonedBoard);
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) return false;

        boolean checkmate = true;

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {

                ChessPosition pos = new BenChessPosition(row, col);

                ChessPiece currentPiece = board.getPiece(pos);

                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> validMoves = validMoves(pos);

                    for (ChessMove move : validMoves) {
                        if (!willCauseCheck(move, teamColor)) {
                            checkmate = false;
                        }
                    }
                }
            }
        }

        return checkmate;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) return false;

        boolean isStalemate = true;

        for (var row = 1; row < 9; row++) {
            for (var col = 1; col < 9; col++) {

                ChessPosition startPosition = new BenChessPosition(row, col);
                ChessPiece pieceToMove = board.getPiece(startPosition);

                if (pieceToMove != null && pieceToMove.getTeamColor() == teamColor) {
                    Collection<ChessMove> validMoves = validMoves(startPosition);

                    for (ChessMove move : validMoves) {
                        ChessPosition endPosition = move.getEndPosition();
                        ChessPiece pieceToReplace = board.getPiece(endPosition);

                        this.board.addPiece(startPosition, null);
                        this.board.addPiece(endPosition, pieceToMove);

                        if (!isInCheck(teamColor)) isStalemate = false;

                        // undo our previous changes
                        this.board.addPiece(startPosition, pieceToMove);
                        this.board.addPiece(endPosition, pieceToReplace);
                    }
                }
            }
        }

        return isStalemate;

    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = (BenChessBoard) board;
    }

    @Override
    public ChessBoard getBoard() {
        return this.board;
    }
}
