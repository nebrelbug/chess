package chess;

import exceptions.ResponseException;

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

    public static BenChessMove fromString(String pos) throws ResponseException {
        if (pos == null || pos.length() != 4) {
            throw new ResponseException(500, "failed to parse chess move");
        }

        String _mov1 = pos.substring(0, 2);
        String _mov2 = pos.substring(2, 4);

        var move1 = BenChessPosition.fromString(_mov1);
        var move2 = BenChessPosition.fromString(_mov2);

        return new BenChessMove(move1, move2, null);
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
    public String toString() {
        return startPosition + " => " + endPosition + " (" + promotionPiece + ")";
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
