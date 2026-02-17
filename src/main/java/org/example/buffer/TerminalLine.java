package org.example.buffer;

import org.example.model.Cell;
import org.example.model.CellAttributes;
import org.example.model.StyleFlags;

import java.util.ArrayList;

/**
 * Represents a single line in a terminal buffer, containing a list of cells.
 */
public class TerminalLine {
    /** The list of cells in the line. */
    private ArrayList<Cell> cells;
    /** The width of the line (number of cells). */
    private int width;
    
    /**
     * Creates a new terminal line with a specified width and initializes cells.
     * @param width The width of the line.
     */
    public TerminalLine(int width) {
        this.width = width;
        this.cells = new ArrayList<>(width);
        for (int i = 0; i < width; i++) {
            this.cells.add(new Cell());
        }
    }
    
    /**
     * Gets the cell at the specified column.
     * @param column The column index.
     * @return The cell at the specified column.
     */
    public Cell getCell(int column) {
        return cells.get(column);
    }
    
    /**
     * Sets the cell at the specified column.
     * @param column The column index.
     * @param cell The cell to set.
     */
    public void setCell(int column, Cell cell){
        this.cells.set(column, cell);
    }
    
    /**
     * Resets all cells in the line to their default state.
     */
    public void clear(){
        for (Cell cell : cells) {
            cell.reset();
        }
    }
    
    /**
     * Fills all cells in the line with a specified character and attributes.
     * @param cell The template cell containing the character.
     * @param attributes The attributes to apply to each cell.
     */
    public void fill(Cell cell, CellAttributes attributes){
        for (int i = 0; i < width; i++) {
            Cell currentCell = cells.get(i);
            currentCell.setCharacter(cell.getCharacter());
            attributes.applyToCell(currentCell);
        }
    }

    /**
     * Returns a string representation of the line (only characters).
     * @return The characters in the line as a string.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Cell cell : cells) {
            sb.append(cell.getCharacter());
        }
        return sb.toString();
    }

    /**
     * Returns a detailed string representation of the line including attributes for each cell.
     * @return A detailed string representation of the line.
     */
    public String toStringWithAttributes() {
        StringBuilder sb = new StringBuilder();
        for (Cell cell : cells) {
            String tmp = "TerminalLine{" +
                    "cells=" + cell.toString() +
                    ", width=" + width +
                    '}';
            sb.append(tmp);
        }
        return sb.toString();
    }

    /**
     * Performs a deep copy of the TerminalLine, ensuring all cells and their
     * styling attributes are copied to a new instance.
     * @return A new TerminalLine instance that is a deep copy of this one.
     */
    public TerminalLine copy(){
        TerminalLine newLine = new TerminalLine(this.width);
        for (int i = 0; i < width; i++) {
            Cell oldCell = this.cells.get(i);
            Cell newCell = newLine.cells.get(i);
            newCell.setCharacter(oldCell.getCharacter());
            newCell.setForegroundColor(oldCell.getForegroundColor());
            newCell.setBackgroundColor(oldCell.getBackgroundColor());
            newCell.setStyleFlags(new StyleFlags(oldCell.getStyleFlags()));
        }
        return newLine;
    }

    /**
     * @return The list of cells in the line.
     */
    public ArrayList<Cell> getCells() {
        return cells;
    }

    /**
     * @param cells The list of cells to set.
     */
    public void setCells(ArrayList<Cell> cells) {
        this.cells = cells;
    }

    /**
     * @return The width of the line.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width The width to set.
     */
    public void setWidth(int width) {
        this.width = width;
    }
}
