package org.example.model;

import java.util.Objects;

/**
 * Represents a single cell in a terminal buffer, containing a character and its styling attributes.
 */
public class Cell {
    /** The character stored in the cell. */
    private char character;
    /** The foreground color of the character. */
    private Color foregroundColor;
    /** The background color of the cell. */
    private Color backgroundColor;
    /** The style flags (bold, italic, underline) applied to the cell. */
    private StyleFlags styleFlags;

    /**
     * Creates a new cell with specified character, colors, and style flags.
     * @param character The character to display.
     * @param foregroundColor The foreground color.
     * @param backgroundColor The background color.
     * @param styleFlags The style flags.
     */
    public Cell(char character, Color foregroundColor, Color backgroundColor, StyleFlags styleFlags){
        this.character = character;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.styleFlags = new StyleFlags(styleFlags);
    }

    /**
     * Creates a default empty cell with a space character and default colors.
     */
    public Cell(){
        this(' ', Color.DEFAULT, Color.DEFAULT, new StyleFlags());
    }

    /**
     * Creates a new cell by copying another cell.
     * @param cell The cell to copy.
     */
    public Cell(Cell cell){
        this(cell.character, cell.foregroundColor, cell.backgroundColor, cell.styleFlags);
    }

    /**
     * @return true if the cell contains only a space character.
     */
    public boolean isEmpty(){
        return character == ' ';
    }

    /**
     * Resets the cell to its default state (space character and default colors).
     */
    public void reset(){
        character = ' ';
        foregroundColor = Color.DEFAULT;
        backgroundColor = Color.DEFAULT;
        styleFlags.reset();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return character == cell.character && foregroundColor == cell.foregroundColor &&
                backgroundColor == cell.backgroundColor &&
                styleFlags.equals(cell.styleFlags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, foregroundColor, backgroundColor, styleFlags);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "character=" + character +
                ", foregroundColor=" + foregroundColor +
                ", backgroundColor=" + backgroundColor +
                "," + styleFlags.toString() +
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
        return foregroundColor;
    }

    /**
     * @param foregroundColor The foreground color to set.
     */
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    /**
     * @return The background color of the cell.
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor The background color to set.
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return The style flags applied to the cell.
     */
    public StyleFlags getStyleFlags() {
        return styleFlags;
    }

    /**
     * @param styleFlags The style flags to set.
     */
    public void setStyleFlags(StyleFlags styleFlags) {
        this.styleFlags = new StyleFlags(styleFlags);
    }
}