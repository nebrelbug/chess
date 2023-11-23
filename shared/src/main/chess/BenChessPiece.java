package chess;

import java.util.Arrays;
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

    @Override
    public char toChar() {
        char res;

        res = switch (this.pieceType) {
            case KING -> '♚';
            case QUEEN -> '♛';
            case BISHOP -> '♝';
            case KNIGHT -> '♞';
            case ROOK -> '♜';
            case PAWN -> '♟';
        };

        return res;
    }

    public String toString() {
        String name = switch (this.pieceType) {
            case KING -> "King";
            case QUEEN -> "Queen";
            case BISHOP -> "Bishop";
            case KNIGHT -> "Knight";
            case ROOK -> "Rook";
            case PAWN -> "Pawn";
        };

        return color + " " + name;
    }

    private ArrayList<ChessPosition> returnValidPositions(ChessBoard board, int currentRow, int currentCol, ArrayList<int[]> moves, boolean isPawn) {
        ArrayList<ChessPosition> res = new ArrayList<>();

        for (int[] move : moves) {
            int horizontalMovement = move[0];
            int verticalMovement = move[1];

            int newRow = currentRow + verticalMovement;
            int newCol = currentCol + horizontalMovement;

            if (newRow < 9 && newCol < 9 && newRow > 0 && newCol > 0) {
                ChessPosition newPosition = new BenChessPosition(newRow, newCol);

                ChessPiece pieceThere = board.getPiece(newPosition);

                if (isPawn) {
                    if (horizontalMovement == 0) { // moving straight
                        if (verticalMovement == 2 || verticalMovement == -2) { // can't move up 2 over another piece
                            ChessPosition intermediatePosition = new BenChessPosition(currentRow + verticalMovement / 2, newCol);
                            if (board.getPiece(intermediatePosition) != null) continue;
                        }

                        if (pieceThere == null) {
                            res.add(newPosition);
                        }
                    } else { // capturing
                        if (pieceThere != null && pieceThere.getTeamColor() != this.color) {
                            res.add(newPosition);
                        }
                    }
                } else if (pieceThere == null || pieceThere.getTeamColor() != this.color) {
                    res.add(newPosition);
                }

            }
        }

        return res;
    }

    private ArrayList<int[]> getStraightPositions(ChessBoard board, ChessPosition myPosition, int xDirection, int yDirection) {

        ArrayList<int[]> moves = new ArrayList<>();

        for (int i = 1; i < 8; i++) {
            int newCol = myPosition.getColumn() + i * xDirection;
            int newRow = myPosition.getRow() + i * yDirection;
            ChessPosition newPosition = new BenChessPosition(newRow, newCol);

            if (newCol < 1 || newRow < 1 || newCol > 8 || newRow > 8) break;

            ChessPiece pieceThere = board.getPiece(newPosition);
            if (pieceThere == null) {
                moves.add(new int[]{i * xDirection, i * yDirection});
            } else if (pieceThere.getTeamColor() == this.color) {
                break;
            } else {
                moves.add(new int[]{i * xDirection, i * yDirection});
                break;
            }
        }

        return moves;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> res = new ArrayList<>();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        switch (this.pieceType) {
            case KING -> {
                ArrayList<int[]> moves = new ArrayList<>(Arrays.asList(
                        new int[]{0, 1},
                        new int[]{0, -1},
                        new int[]{1, 1},
                        new int[]{1, -1},
                        new int[]{-1, 1},
                        new int[]{-1, -1},
                        new int[]{1, 0},
                        new int[]{-1, 0}
                ));

                ArrayList<ChessPosition> validPositions = returnValidPositions(board, currentRow, currentCol, moves, false);

                for (ChessPosition position : validPositions) {
                    res.add(new BenChessMove(myPosition, position, null));
                }
            }
            case QUEEN -> {
                ArrayList<int[]> moves = new ArrayList<>();

                moves.addAll(getStraightPositions(board, myPosition, 1, 0));
                moves.addAll(getStraightPositions(board, myPosition, -1, 0));
                moves.addAll(getStraightPositions(board, myPosition, 0, 1));
                moves.addAll(getStraightPositions(board, myPosition, 0, -1));
                moves.addAll(getStraightPositions(board, myPosition, 1, 1));
                moves.addAll(getStraightPositions(board, myPosition, 1, -1));
                moves.addAll(getStraightPositions(board, myPosition, -1, 1));
                moves.addAll(getStraightPositions(board, myPosition, -1, -1));

                ArrayList<ChessPosition> validPositions = returnValidPositions(board, currentRow, currentCol, moves, false);

                for (ChessPosition position : validPositions) {
                    res.add(new BenChessMove(myPosition, position, null));
                }
            }
            case ROOK -> {
                ArrayList<int[]> moves = new ArrayList<>();

                moves.addAll(getStraightPositions(board, myPosition, 1, 0));
                moves.addAll(getStraightPositions(board, myPosition, -1, 0));
                moves.addAll(getStraightPositions(board, myPosition, 0, 1));
                moves.addAll(getStraightPositions(board, myPosition, 0, -1));

                ArrayList<ChessPosition> validPositions = returnValidPositions(board, currentRow, currentCol, moves, false);

                for (ChessPosition position : validPositions) {
                    res.add(new BenChessMove(myPosition, position, null));
                }
            }
            case BISHOP -> {

                ArrayList<int[]> moves = new ArrayList<>();

                moves.addAll(getStraightPositions(board, myPosition, 1, 1));
                moves.addAll(getStraightPositions(board, myPosition, 1, -1));
                moves.addAll(getStraightPositions(board, myPosition, -1, 1));
                moves.addAll(getStraightPositions(board, myPosition, -1, -1));

                ArrayList<ChessPosition> validPositions = returnValidPositions(board, currentRow, currentCol, moves, false);

                for (ChessPosition position : validPositions) {
                    res.add(new BenChessMove(myPosition, position, null));
                }

            }
            case KNIGHT -> {

                ArrayList<int[]> moves = new ArrayList<>(Arrays.asList(
                        new int[]{1, 2},
                        new int[]{-1, 2},
                        new int[]{2, 1},
                        new int[]{-2, 1},
                        new int[]{1, -2},
                        new int[]{-1, -2},
                        new int[]{2, -1},
                        new int[]{-2, -1}
                ));

                ArrayList<ChessPosition> validPositions = returnValidPositions(board, currentRow, currentCol, moves, false);

                for (ChessPosition position : validPositions) {
                    res.add(new BenChessMove(myPosition, position, null));
                }
            }
            case PAWN -> {

                boolean canMoveTwo = (color == ChessGame.TeamColor.WHITE && currentRow == 2) || (color == ChessGame.TeamColor.BLACK && currentRow == 7);
                int forward = (color == ChessGame.TeamColor.WHITE ? 1 : -1);

                int[] moveForward = {0, forward};
                int[] moveDoubleForward = new int[]{0, forward * 2};
                int[] captureLeft = {-1, forward};
                int[] captureRight = {1, forward};

                ArrayList<int[]> moves = new ArrayList<>(Arrays.asList(captureLeft, captureRight, moveForward));

                if (canMoveTwo) moves.add(moveDoubleForward);

                ArrayList<ChessPosition> validPositions = returnValidPositions(board, currentRow, currentCol, moves, true);

                for (ChessPosition position : validPositions) {
                    boolean deservesPromotion = position.getRow() == 1 || position.getRow() == 8;

                    if (deservesPromotion) {
                        res.add(new BenChessMove(myPosition, position, PieceType.QUEEN));
                        res.add(new BenChessMove(myPosition, position, PieceType.ROOK));
                        res.add(new BenChessMove(myPosition, position, PieceType.BISHOP));
                        res.add(new BenChessMove(myPosition, position, PieceType.KNIGHT));

                    } else {
                        res.add(new BenChessMove(myPosition, position, null));
                    }
                }

            }
        }

        return res;
    }
}
