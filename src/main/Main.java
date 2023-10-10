import chess.*;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        BenChessBoard board = new BenChessBoard();

        ChessPiece bishop = new BenChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPosition whitePosition = new BenChessPosition(2, 7);
        board.addPiece(whitePosition, bishop);

        board.addPiece(new BenChessPosition(3, 6), new BenChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        System.out.println(board);

        Collection<ChessMove> moves = bishop.pieceMoves(board, whitePosition);

        for (ChessMove move : moves) {
            System.out.println(move);
        }


//        Set<ChessMove> pieceMoves = new HashSet<>(pawn.pieceMoves(board, position));
//        Assertions.assertTrue(pieceMoves.isEmpty(),


    }
}