package chess;

import java.util.Arrays;

public class BenChessBoard implements ChessBoard {

    private final ChessPiece[][] board;

    public BenChessBoard() {
        board = new ChessPiece[8][8];
    }

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow();
        int col = position.getColumn();

        board[8 - row][col - 1] = piece;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return board[8 - row][col - 1];
    }

    @Override
    public void resetBoard() {

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < 8; row++) {
            if (row != 0) sb.append("\n");
            sb.append("|");
            for (int col = 0; col < 8; col++) {
                char piece = board[row][col] == null ? ' ' : board[row][col].toChar();

                sb.append(piece).append("|");
            }
        }
        return sb.toString();
    }
}
