package chess;

import java.util.Collection;
import java.util.ArrayList;

public class BenChessPiece implements ChessPiece {

    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType pieceType;

    public BenChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        pieceType = type;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    @Override
    public PieceType getPieceType() {
        return pieceType;
    }

    public char toChar() {
        return switch (this.pieceType) {
            case KING -> 'K';
            case QUEEN -> 'Q';
            case BISHOP -> 'B';
            case KNIGHT -> 'H';
            case ROOK -> 'R';
            case PAWN -> 'p';
        };
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> res = new ArrayList<ChessMove>();

        switch (this.pieceType) {
            case KING -> {

            }
            case QUEEN -> {
            }
            case BISHOP -> {
            }
            case KNIGHT -> {
            }
            case ROOK -> {
            }
            case PAWN -> {

                // ONE FORWARD
                int newRow = myPosition.getRow() + (color == ChessGame.TeamColor.WHITE ? 1 : -1);

                PieceType promotionPiece = null;

                if (newRow == 1 || newRow == 8) {
                    promotionPiece = PieceType.QUEEN; // TODO: allow customizing
                }

                ChessPosition straightForward = new BenChessPosition(newRow, myPosition.getColumn());
                if (board.getPiece(straightForward) == null) {
                    res.add(new BenChessMove(myPosition, straightForward, promotionPiece));
                }
                if (myPosition.getColumn() > 1) {
                    ChessPosition leftForward = new BenChessPosition(newRow, myPosition.getColumn() - 1);
                    if (board.getPiece(straightForward) != null && board.getPiece(straightForward).getPieceType() != this.pieceType) {
                        res.add(new BenChessMove(myPosition, leftForward, promotionPiece));
                    }
                }
                if (myPosition.getColumn() < 8) {
                    ChessPosition rightForward = new BenChessPosition(newRow, myPosition.getColumn() + 1);
                    if (board.getPiece(rightForward) != null && board.getPiece(rightForward).getPieceType() != this.pieceType) {
                        res.add(new BenChessMove(myPosition, rightForward, promotionPiece));
                    }
                }


                // WHITE -- two forward
                if (color == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) {
                    ChessPosition newPosition = new BenChessPosition(4, myPosition.getColumn());

                    if (board.getPiece(newPosition) == null) {
                        res.add(new BenChessMove(myPosition, newPosition, null));
                    }
                }

                // BLACK -- two forward
                if (color == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7) {
                    ChessPosition newPosition = new BenChessPosition(5, myPosition.getColumn());

                    if (board.getPiece(newPosition) == null) {
                        res.add(new BenChessMove(myPosition, newPosition, null));
                    }
                }
            }
        }
        ;

        return res;
    }
}
