package ui;

import chess.*;
import models.Game;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static ui.EscapeSequences.*;

public class BoardDisplay {

    public static String display(Game game, ChessGame.TeamColor perspective, ChessPosition pos) {
        var board = game.game().getBoard();

        return display(board, perspective, pos);
    }

    public static String display(ChessBoard board, ChessGame.TeamColor perspective, ChessPosition pos) {

        var game = new BenChessGame();
        game.setBoard(board);

        var validMoves = pos == null ? List.of() : game.validMoves(pos);

        StringBuilder sb = new StringBuilder();

        List<Integer> rows = Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1);
        List<Integer> cols = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<String> colNames = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");


        if (perspective == ChessGame.TeamColor.BLACK) {
            Collections.reverse(rows);
            Collections.reverse(cols);
            Collections.reverse(colNames);
        }

        sb.append(SET_BG_COLOR_LIGHT_GREY)
                .append(SET_TEXT_COLOR_BLACK)
                .append("    ")
                .append(String.join("  ", colNames))
                .append("    ")
                .append(RESET_BG_COLOR)
                .append("\n");

        for (int row : rows) {
            sb.append(SET_BG_COLOR_LIGHT_GREY)
                    .append(" ")
                    .append(row)
                    .append(" ")
                    .append(RESET_BG_COLOR);

            for (int col : cols) {

                var thisPosition = new BenChessPosition(row, col);
                var thisMove = new BenChessMove(pos, thisPosition, null);

                var piece = board.getPiece(thisPosition);

                String pieceString = " ";

                if (piece != null) {
                    pieceString = "" + piece.toChar();

                    var color = piece.getTeamColor();

                    if (color == ChessGame.TeamColor.BLACK) {
                        pieceString = SET_TEXT_COLOR_BLACK + pieceString + RESET_TEXT_COLOR;
                    } else {
                        pieceString = SET_TEXT_COLOR_WHITE + pieceString + RESET_TEXT_COLOR;
                    }
                }

                boolean isHighlighted = !validMoves.isEmpty() && validMoves.contains(thisMove);

                String squareBackground = isHighlighted ?
                        ((row + col) % 2 == 0 ? SET_BG_COLOR_DARK_BLUE : SET_BG_COLOR_BLUE) :
                        ((row + col) % 2 == 0 ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_GREEN);

                sb.append(squareBackground)
                        .append(" ")
                        .append(pieceString)
                        .append(" ")
                        .append(RESET_BG_COLOR);
            }

            sb.append(SET_BG_COLOR_LIGHT_GREY)
                    .append(" ")
                    .append(row)
                    .append(" ")
                    .append(RESET_BG_COLOR);

            sb.append("\n");

        }

        sb.append(SET_BG_COLOR_LIGHT_GREY)
                .append("    ")
                .append(String.join("  ", colNames))
                .append("    ")
                .append(RESET_BG_COLOR);

        return sb.toString();
    }

}
