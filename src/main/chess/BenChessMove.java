package chess;

import java.util.Objects;

public class BenChessMove implements ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public BenChessMove(ChessPosition moveStartPosition, ChessPosition moveEndPosition, ChessPiece.PieceType movePromotionPiece) {
        startPosition = moveStartPosition;
        endPosition = moveEndPosition;
        promotionPiece = movePromotionPiece;
    }

    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BenChessMove that = (BenChessMove) o;
        return Objects.equals(startPosition, that.startPosition) && Objects.equals(endPosition, that.endPosition) && promotionPiece == that.promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }
}
