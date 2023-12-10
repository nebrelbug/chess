package chess;

import exceptions.ResponseException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BenChessPosition implements ChessPosition {

    private final int row;
    private final int col;

    public BenChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static BenChessPosition fromString(String pos) throws ResponseException {
        List<String> colNames = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");

        if (pos == null || pos.length() != 2) {
            throw new ResponseException(500, "failed to parse chess position");
        }

        String colName = pos.substring(0, 1);
        int row = Integer.parseInt(pos.substring(1, 2));

        if (row < 1 || row > 8 || !colNames.contains(colName)) {
            throw new ResponseException(500, "failed to parse chess position");
        }

        int col = colNames.indexOf(colName) + 1;

        return new BenChessPosition(row, col);

    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return col;
    }

    public String toString() {
        return "(" + row + ", " + col + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BenChessPosition that = (BenChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
