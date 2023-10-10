package chess;

import java.util.Arrays;

public class BenChessBoard implements ChessBoard {

    private final ChessPiece[][] board;

    private void addWhitePiece(int row, int col, ChessPiece.PieceType pieceType) {
        addPiece(new BenChessPosition(row, col), new BenChessPiece(ChessGame.TeamColor.WHITE, pieceType));
    }

    private void addBlackPiece(int row, int col, ChessPiece.PieceType pieceType) {
        addPiece(new BenChessPosition(row, col), new BenChessPiece(ChessGame.TeamColor.BLACK, pieceType));
    }

    private void initializeBoard() {
        /* INITIALIZE WHITE PIECES */

        for (int i = 1; i < 9; i++) {
            addWhitePiece(2, i, ChessPiece.PieceType.PAWN);
        }

        addWhitePiece(1, 1, ChessPiece.PieceType.ROOK);
        addWhitePiece(1, 8, ChessPiece.PieceType.ROOK);
        addWhitePiece(1, 2, ChessPiece.PieceType.KNIGHT);
        addWhitePiece(1, 7, ChessPiece.PieceType.KNIGHT);
        addWhitePiece(1, 3, ChessPiece.PieceType.BISHOP);
        addWhitePiece(1, 6, ChessPiece.PieceType.BISHOP);
        addWhitePiece(1, 4, ChessPiece.PieceType.QUEEN);
        addWhitePiece(1, 5, ChessPiece.PieceType.KING);

        /* INITIALIZE BLACK PIECES */

        for (int i = 1; i < 9; i++) {
            addBlackPiece(7, i, ChessPiece.PieceType.PAWN);
        }

        addBlackPiece(8, 1, ChessPiece.PieceType.ROOK);
        addBlackPiece(8, 8, ChessPiece.PieceType.ROOK);
        addBlackPiece(8, 2, ChessPiece.PieceType.KNIGHT);
        addBlackPiece(8, 7, ChessPiece.PieceType.KNIGHT);
        addBlackPiece(8, 3, ChessPiece.PieceType.BISHOP);
        addBlackPiece(8, 6, ChessPiece.PieceType.BISHOP);
        addBlackPiece(8, 4, ChessPiece.PieceType.QUEEN);
        addBlackPiece(8, 5, ChessPiece.PieceType.KING);
    }

    public BenChessBoard() {
        this.board = new ChessPiece[8][8];

//        initializeBoard();
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

    public void emptyBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
    }

    @Override
    public void resetBoard() {
        emptyBoard();
        initializeBoard();
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
