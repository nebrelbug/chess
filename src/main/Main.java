import chess.*;

public class Main {
    public static void main(String[] args) {
        ChessBoard board = new BenChessBoard();

        board.addPiece(new BenChessPosition(2, 4), new BenChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));

        System.out.println(board);
    }
}