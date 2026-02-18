package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

    @Test
    void defaultConstructorCreatesEmptyCell() {
        Cell cell = new Cell();
        assertEquals(' ', cell.getCharacter());
        assertEquals(Color.DEFAULT, cell.getForegroundColor());
        assertEquals(Color.DEFAULT, cell.getBackgroundColor());
        assertFalse(cell.getStyleFlags().getBold());
        assertFalse(cell.getStyleFlags().getItalic());
        assertFalse(cell.getStyleFlags().getUnderline());
    }

    @Test
    void copyConstructorCreatesDeepCopyOfStyleFlags() {
        StyleFlags flags = new StyleFlags(true, false, true);
        Cell original = new Cell('A', Color.RED, Color.BLUE, flags);
        Cell copy = new Cell(original);

        // Mutate original after copy
        original.getStyleFlags().reset();
        original.setCharacter('B');
        original.setForegroundColor(Color.GREEN);
        original.setBackgroundColor(Color.CYAN);

        // Verify copy remains unchanged
        assertEquals('A', copy.getCharacter());
        assertEquals(Color.RED, copy.getForegroundColor());
        assertEquals(Color.BLUE, copy.getBackgroundColor());
        assertTrue(copy.getStyleFlags().getBold());
        assertFalse(copy.getStyleFlags().getItalic());
        assertTrue(copy.getStyleFlags().getUnderline());
    }

    @Test
    void resetRestoresDefaults() {
        Cell cell = new Cell('Z', Color.WHITE, Color.BLACK, new StyleFlags(true, true, true));
        cell.reset();
        assertEquals(' ', cell.getCharacter());
        assertEquals(Color.DEFAULT, cell.getForegroundColor());
        assertEquals(Color.DEFAULT, cell.getBackgroundColor());
        assertFalse(cell.getStyleFlags().getBold());
        assertFalse(cell.getStyleFlags().getItalic());
        assertFalse(cell.getStyleFlags().getUnderline());
    }

    @Test
    void settersUseDefensiveCopyForStyleFlags() {
        Cell cell = new Cell();
        StyleFlags input = new StyleFlags(true, true, false);
        cell.setStyleFlags(input);
        // Mutate the source object; cell should not be affected because of defensive copy
        input.reset();
        assertTrue(cell.getStyleFlags().getBold());
        assertTrue(cell.getStyleFlags().getItalic());
        assertFalse(cell.getStyleFlags().getUnderline());
    }
}