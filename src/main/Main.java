import chess.*;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import response.Stringifier;

public class Main {
    public static void main(String[] args) throws InvalidMoveException, DataAccessException {
        BenChessBoard board = new BenChessBoard();
        ChessGame game = new BenChessGame();

        board.addPiece(new BenChessPosition(8, 8), new BenChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        board.addPiece(new BenChessPosition(4, 4), new BenChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        board.addPiece(new BenChessPosition(2, 2), new BenChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));

//        board.addPiece(new BenChessPosition(2, 7), new BenChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));

        game.setBoard(board);
        game.setTeamTurn(ChessGame.TeamColor.BLACK);

        System.out.println(board);

//        game.makeMove(new BenChessMove(new BenChessPosition(4, 4), new BenChessPosition(3, 4), null));

        System.out.println(game.isInCheckmate(ChessGame.TeamColor.BLACK));

//        System.out.println(board);

        var dao = new GameDAO();

        dao.updateGame(3, Stringifier.jsonify(game));

    }
}