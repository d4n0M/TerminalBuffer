package org.example.buffer;

import org.example.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalLineTest {

    @Test
    void constructorInitializesWidthAndCells() {
        TerminalLine line = new TerminalLine(5);
        assertEquals(5, line.getWidth());
        for (int i = 0; i < 5; i++) {
            assertTrue(line.getCell(i).isEmpty());
        }
    }

    @Test
    void setAndGetCellWorks() {
        TerminalLine line = new TerminalLine(3);
        Cell cell = new Cell('X', Color.RED, Color.BLUE, new StyleFlags(true, false, false));
        line.setCell(1, cell);
        assertEquals('X', line.getCell(1).getCharacter());
        assertEquals(Color.RED, line.getCell(1).getForegroundColor());
        assertEquals(Color.BLUE, line.getCell(1).getBackgroundColor());
    }

    @Test
    void clearResetsAllCells() {
        TerminalLine line = new TerminalLine(2);
        line.getCell(0).setCharacter('X');
        line.getCell(1).setCharacter('Y');
        line.clear();
        assertTrue(line.getCell(0).isEmpty());
        assertTrue(line.getCell(1).isEmpty());
    }

    @Test
    void fillAppliesCharacterAndAttributes() {
        TerminalLine line = new TerminalLine(4);
        Cell template = new Cell('A', Color.GREEN, Color.CYAN, new StyleFlags(false, true, true));
        CellAttributes attrs = new CellAttributes(Color.GREEN, Color.CYAN, new StyleFlags(false, true, true));
        line.fill(template, attrs);
        for (int i = 0; i < 4; i++) {
            Cell c = line.getCell(i);
            assertEquals('A', c.getCharacter());
            assertEquals(Color.GREEN, c.getForegroundColor());
            assertEquals(Color.CYAN, c.getBackgroundColor());
            assertFalse(c.getStyleFlags().getBold());
            assertTrue(c.getStyleFlags().getItalic());
            assertTrue(c.getStyleFlags().getUnderline());
        }
    }

    @Test
    void copyCreatesDeepCopy() {
        TerminalLine line = new TerminalLine(2);
        line.getCell(0).setCharacter('Q');
        line.getCell(0).setStyleFlags(new StyleFlags(true, false, false));

        TerminalLine copy = line.copy();

        // Mutate original after copy
        line.getCell(0).setCharacter('W');
        line.getCell(0).getStyleFlags().reset();

        // Verify copy remains unchanged
        assertEquals('Q', copy.getCell(0).getCharacter());
        assertTrue(copy.getCell(0).getStyleFlags().getBold());
    }

    @Test
    void setWidthExpandsAndTrims() {
        TerminalLine line = new TerminalLine(2);
        line.setWidth(4);
        assertEquals(4, line.getWidth());
        for (int i = 0; i < 4; i++) {
            assertNotNull(line.getCell(i));
        }
        line.setWidth(1);
        assertEquals(1, line.getWidth());
    }
}