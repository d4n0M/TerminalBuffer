package org.example.model;

import java.util.Objects;

/**
 * Represents a single cell in a terminal buffer, containing a character and its styling attributes.
 */
public class Cell {
    /** The character stored in the cell. */
    private char character;
    /** The cell attributes (colors and styles). */
    private CellAttributes attributes;

    /**
     * Creates a new cell with specified character and attributes.
     * @param character The character to display.
     * @param attributes The cell attributes (colors and styles).
     */
    public Cell(char character, CellAttributes attributes) {
        this.character = character;
        this.attributes = new CellAttributes(attributes);
    }

    /**
     * Creates a new cell with specified character, colors, and style flags.
     * @param character The character to display.
     * @param foregroundColor The foreground color.
     * @param backgroundColor The background color.
     * @param styleFlags The style flags.
     */
    public Cell(char character, Color foregroundColor, Color backgroundColor, StyleFlags styleFlags) {
        this.character = character;
        this.attributes = new CellAttributes(foregroundColor, backgroundColor, styleFlags);
    }

    /**
     * Creates a default empty cell with a space character and default colors.
     */
    public Cell() {
        this(' ', new CellAttributes());
    }

    /**
     * Creates a new cell by copying another cell.
     * @param cell The cell to copy.
     */
    public Cell(Cell cell) {
        this(cell.character, cell.attributes);
    }

    /**
     * @return true if the cell contains only a space character.
     */
    public boolean isEmpty() {
        return character == ' ';
    }

    /**
     * Resets the cell to its default state (space character and default colors).
     */
    public void reset() {
        character = ' ';
        attributes.reset();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return character == cell.character && attributes.equals(cell.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, attributes);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "character=" + character +
                ", " + attributes.toString() +
                '}';
    }

    /**
     * @return The character stored in the cell.
     */
    public char getCharacter() {
        return character;
    }

    /**
     * @param character The character to set.
     */
    public void setCharacter(char character) {
        this.character = character;
    }

    /**
     * @return The foreground color of the cell.
     */
    public Color getForegroundColor() {
        return attributes.getForegroundColor();
    }

    /**
     * @param foregroundColor The foreground color to set.
     */
    public void setForegroundColor(Color foregroundColor) {
        attributes.setForegroundColor(foregroundColor);
    }

    /**
     * @return The background color of the cell.
     */
    public Color getBackgroundColor() {
        return attributes.getBackgroundColor();
    }

    /**
     * @param backgroundColor The background color to set.
     */
    public void setBackgroundColor(Color backgroundColor) {
        attributes.setBackgroundColor(backgroundColor);
    }

    /**
     * @return The style flags applied to the cell.
     */
    public StyleFlags getStyle() {
        return attributes.getStyle();
    }

    /**
     * @param styleFlags The style flags to set.
     */
    public void setStyle(StyleFlags styleFlags) {
        attributes.setStyle(styleFlags);
    }

    /**
     * @return A copy of the cell attributes.
     */
    public CellAttributes getAttributes() {
        return attributes;
    }

    /**
     * @param attributes The cell attributes to set.
     */
    public void setAttributes(CellAttributes attributes) {
        this.attributes = attributes;
    }
}