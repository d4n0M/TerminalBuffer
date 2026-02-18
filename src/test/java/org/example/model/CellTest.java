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
        assertFalse(cell.getStyle().getBold());
        assertFalse(cell.getStyle().getItalic());
        assertFalse(cell.getStyle().getUnderline());
    }

    @Test
    void copyConstructorCreatesDeepCopyOfStyleFlags() {
        StyleFlags flags = new StyleFlags(true, false, true);
        Cell original = new Cell('A', Color.RED, Color.BLUE, flags);
        Cell copy = new Cell(original);

        // Mutate original after copy
        original.getStyle().reset();
        original.setCharacter('B');
        original.setForegroundColor(Color.GREEN);
        original.setBackgroundColor(Color.CYAN);

        // Verify copy remains unchanged
        assertEquals('A', copy.getCharacter());
        assertEquals(Color.RED, copy.getForegroundColor());
        assertEquals(Color.BLUE, copy.getBackgroundColor());
        assertTrue(copy.getStyle().getBold());
        assertFalse(copy.getStyle().getItalic());
        assertTrue(copy.getStyle().getUnderline());
    }

    @Test
    void resetRestoresDefaults() {
        Cell cell = new Cell('Z', Color.WHITE, Color.BLACK, new StyleFlags(true, true, true));
        cell.reset();
        assertEquals(' ', cell.getCharacter());
        assertEquals(Color.DEFAULT, cell.getForegroundColor());
        assertEquals(Color.DEFAULT, cell.getBackgroundColor());
        assertFalse(cell.getStyle().getBold());
        assertFalse(cell.getStyle().getItalic());
        assertFalse(cell.getStyle().getUnderline());
    }

    @Test
    void settersUseDefensiveCopyForStyleFlags() {
        Cell cell = new Cell();
        StyleFlags input = new StyleFlags(true, true, false);
        cell.setStyle(input);
        // Mutate the source object; cell should not be affected because of defensive copy
        input.reset();
        assertTrue(cell.getStyle().getBold());
        assertTrue(cell.getStyle().getItalic());
        assertFalse(cell.getStyle().getUnderline());
    }
}