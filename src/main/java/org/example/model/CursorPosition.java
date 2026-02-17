package org.example.model;

import java.util.Objects;

/**
 * Represents a position (column and row) in a terminal buffer.
 */
public class CursorPosition {
    /** The column index of the cursor. */
    private int column;
    /** The row index of the cursor. */
    private int row;

    /**
     * Creates a new cursor position with the specified column and row.
     * @param column The column index.
     * @param row The row index.
     */
    public CursorPosition(int column, int row){
        this.column = column;
        this.row = row;
    }

    /**
     * Checks if the position is within the boundaries of a given area.
     * @param width The width of the area.
     * @param height The height of the area.
     * @return true if the position is within the area (column >= 0 and column < width, and row >= 0 and row < height).
     */
    public boolean isValid(int width, int height){
        return column >= 0 && column < width && row >= 0 && row < height;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CursorPosition that = (CursorPosition) o;
        return column == that.column && row == that.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }

    /**
     * @return The column index.
     */
    public int getColumn() {
        return column;
    }

    /**
     * @param column The column index to set.
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * @return The row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row The row index to set.
     */
    public void setRow(int row) {
        this.row = row;
    }
}