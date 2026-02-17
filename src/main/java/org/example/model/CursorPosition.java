package org.example.model;

import java.util.Objects;

public class CursorPosition {
    private int column;
    private int row;

    public CursorPosition(int column, int row){
        this.column = column;
        this.row = row;
    }

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

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}