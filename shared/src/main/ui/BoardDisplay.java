package ui;

import chess.BenChessPosition;
import chess.ChessBoard;
import chess.ChessGame;
import models.Game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ui.EscapeSequences.*;

public class BoardDisplay {

    public static String display(Game game, boolean blackPerspective) {
        var board = game.game().getBoard();

        return display(board, blackPerspective);
    }

    public static String display(ChessBoard board, boolean blackPerspective) {

        StringBuilder sb = new StringBuilder();

        List<Integer> rows = Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1);
        List<Integer> cols = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<String> colNames = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");


        if (blackPerspective) {
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

                var piece = board.getPiece(new BenChessPosition(row, col));

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


                var squareBackground = (row + col) % 2 == 0 ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_GREEN;

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
