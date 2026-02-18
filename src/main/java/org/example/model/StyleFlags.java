package org.example.model;

import java.util.Objects;

/**
 * Represents style attributes for a terminal cell, such as bold, italic, and underline.
 */
public class StyleFlags{
    /** Whether the text is bold. */
    private boolean bold;
    /** Whether the text is italic. */
    private boolean italic;
    /** Whether the text is underlined. */
    private boolean underline;

    /**
     * Creates a new set of style flags with all styles disabled.
     */
    public StyleFlags(){
        this.bold = false;
        this.italic = false;
        this.underline = false;
    }

    /**
     * Creates a new set of style flags by copying another set.
     * @param styleFlags The style flags to copy.
     */
    public StyleFlags(StyleFlags styleFlags){
        this.bold = styleFlags.bold;
        this.italic = styleFlags.italic;
        this.underline = styleFlags.underline;
    }

    /**
     * Creates a new set of style flags with specified values.
     * @param bold Whether the text is bold.
     * @param italic Whether the text is italic.
     * @param underline Whether the text is underlined.
     */
    public StyleFlags(boolean bold, boolean italic, boolean underline){
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
    }

    /**
     * Resets all style flags to false.
     */
    public void reset(){
        this.bold = false;
        this.italic = false;
        this.underline = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StyleFlags other)) return false;
        return bold == other.bold
                && italic == other.italic
                && underline == other.underline;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bold, italic, underline);
    }

    /**
     * @return true if the text is bold.
     */
    public boolean getBold() {
        return bold;
    }

    /**
     * @return true if the text is italic.
     */
    public boolean getItalic() {
        return italic;
    }

    /**
     * @return true if the text is underlined.
     */
    public boolean getUnderline() {
        return underline;
    }
}