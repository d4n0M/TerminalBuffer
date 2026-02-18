package org.example.buffer;

import org.example.model.CellAttributes;
import org.example.model.Color;
import org.example.model.CursorPosition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalBufferTest {

    @Test
    void canInstantiateWithCorrectInitialState() {
        int width = 80;
        int height = 24;
        int maxScrollback = 100;
        TerminalBuffer buffer = new TerminalBuffer(width, height, maxScrollback);
        
        assertNotNull(buffer);
        assertEquals(width, buffer.getWidth());
        assertEquals(height, buffer.getHeight());
        assertEquals(maxScrollback, buffer.getMaxScrollbackLines());
        
        // Check screen initialization
        assertNotNull(buffer.getScreen());
        assertEquals(height, buffer.getScreen().size());
        for (TerminalLine line : buffer.getScreen()) {
            assertNotNull(line);
            assertEquals(width, line.getWidth());
        }
        
        // Check scrollback initialization
        assertNotNull(buffer.getScrollback());
        assertTrue(buffer.getScrollback().isEmpty());
        
        // Check cursor initialization
        assertNotNull(buffer.getCursor());
        assertEquals(0, buffer.getCursor().getColumn());
        assertEquals(0, buffer.getCursor().getRow());
        
        // Check attributes initialization
        assertNotNull(buffer.getCurrentAttributes());
    }

    @Test
    void basicValueTest(){
        int width = 80;
        int height = 24;
        int maxScrollback = 100;
        TerminalBuffer buffer = new TerminalBuffer(width, height, maxScrollback);
        assertNotNull(buffer);
        assertEquals(width, buffer.getWidth());
        assertEquals(height, buffer.getHeight());

        assertEquals(0, buffer.getCursor().getColumn());
        assertEquals(0, buffer.getCursor().getRow());

        assertEquals(' ', buffer.getScreen().getFirst().getCell(0).getCharacter());
    }

    // ==================== Screen Initialization Tests ====================

    @Test
    void initializeScreenWithEmptyLines_standardSize() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        
        assertEquals(24, buffer.getScreen().size());
        for (TerminalLine line : buffer.getScreen()) {
            assertNotNull(line);
            assertEquals(80, line.getWidth());
            // Verify all cells are empty (space character)
            for (int i = 0; i < 80; i++) {
                assertEquals(' ', line.getCell(i).getCharacter());
            }
        }
    }

    @Test
    void initializeScreenWithEmptyLines_minimumSize() {
        TerminalBuffer buffer = new TerminalBuffer(1, 1, 0);
        
        assertEquals(1, buffer.getScreen().size());
        assertEquals(1, buffer.getScreen().get(0).getWidth());
        assertEquals(' ', buffer.getScreen().get(0).getCell(0).getCharacter());
    }

    @Test
    void initializeScreenWithEmptyLines_largeSize() {
        TerminalBuffer buffer = new TerminalBuffer(500, 200, 1000);
        
        assertEquals(200, buffer.getScreen().size());
        for (TerminalLine line : buffer.getScreen()) {
            assertEquals(500, line.getWidth());
        }
    }

    @Test
    void initializeScreenWithEmptyLines_allLinesAreDistinctObjects() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 10);
        
        for (int i = 0; i < buffer.getScreen().size(); i++) {
            for (int j = i + 1; j < buffer.getScreen().size(); j++) {
                assertNotSame(buffer.getScreen().get(i), buffer.getScreen().get(j),
                        "Lines " + i + " and " + j + " should be distinct objects");
            }
        }
    }

    // ==================== Scrollback Initialization Tests ====================

    @Test
    void initializeEmptyScrollback_withZeroMaxLines() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 0);
        
        assertNotNull(buffer.getScrollback());
        assertTrue(buffer.getScrollback().isEmpty());
        assertEquals(0, buffer.getMaxScrollbackLines());
    }

    // ==================== Default Attributes Tests ====================

    @Test
    void setDefaultAttributes_isNotNull() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        assertNotNull(buffer.getCurrentAttributes());
    }

    @Test
    void setDefaultAttributes_defaultForegroundColor() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        
        CellAttributes attrs = buffer.getCurrentAttributes();
        assertEquals(Color.DEFAULT, attrs.getForegroundColor());
    }

    @Test
    void setDefaultAttributes_defaultBackgroundColor() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        
        CellAttributes attrs = buffer.getCurrentAttributes();
        assertEquals(Color.DEFAULT, attrs.getBackgroundColor());
    }

    @Test
    void setDefaultAttributes_styleIsNotNull() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        
        CellAttributes attrs = buffer.getCurrentAttributes();
        assertNotNull(attrs.getStyle());
    }

    @Test
    void setDefaultAttributes_styleHasNoFlags() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        
        CellAttributes attrs = buffer.getCurrentAttributes();
        assertFalse(attrs.getStyle().getBold());
        assertFalse(attrs.getStyle().getItalic());
        assertFalse(attrs.getStyle().getUnderline());
    }

    @Test
    void setDefaultAttributes_withDifferentBufferSizes() {
        TerminalBuffer buffer1 = new TerminalBuffer(1, 1, 0);
        TerminalBuffer buffer2 = new TerminalBuffer(1000, 500, 10000);
        
        // Both should have the same default attributes
        assertEquals(buffer1.getCurrentAttributes().getForegroundColor(),
                buffer2.getCurrentAttributes().getForegroundColor());
        assertEquals(buffer1.getCurrentAttributes().getBackgroundColor(),
                buffer2.getCurrentAttributes().getBackgroundColor());
    }

    @Test
    void setDefaultAttributes_attributesAreIndependentPerBuffer() {
        TerminalBuffer buffer1 = new TerminalBuffer(80, 24, 100);
        TerminalBuffer buffer2 = new TerminalBuffer(80, 24, 100);
        
        assertNotSame(buffer1.getCurrentAttributes(), buffer2.getCurrentAttributes(),
                "Each buffer should have its own CellAttributes instance");
    }

    // ==================== setForegroundColor Tests ====================

    @Test
    void setForegroundColor_changesCurrentAttributes() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setForegroundColor(Color.RED);
        assertEquals(Color.RED, buffer.getCurrentAttributes().getForegroundColor());
    }

    @Test
    void setForegroundColor_nullThrowsException() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        assertThrows(IllegalArgumentException.class, () -> buffer.setForegroundColor(null));
    }

    @Test
    void setForegroundColor_doesNotAffectBackgroundColor() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setForegroundColor(Color.RED);
        assertEquals(Color.DEFAULT, buffer.getCurrentAttributes().getBackgroundColor());
    }

    // ==================== setBackgroundColor Tests ====================

    @Test
    void setBackgroundColor_changesCurrentAttributes() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setBackgroundColor(Color.BLUE);
        assertEquals(Color.BLUE, buffer.getCurrentAttributes().getBackgroundColor());
    }

    @Test
    void setBackgroundColor_nullThrowsException() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        assertThrows(IllegalArgumentException.class, () -> buffer.setBackgroundColor(null));
    }

    @Test
    void setBackgroundColor_doesNotAffectForegroundColor() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setBackgroundColor(Color.BLUE);
        assertEquals(Color.DEFAULT, buffer.getCurrentAttributes().getForegroundColor());
    }

    // ==================== setBold Tests ====================

    @Test
    void setBold_enablesBoldFlag() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setBold(true);
        assertTrue(buffer.getCurrentAttributes().getStyle().getBold());
    }

    @Test
    void setBold_disablesBoldFlag() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setBold(true);
        buffer.setBold(false);
        assertFalse(buffer.getCurrentAttributes().getStyle().getBold());
    }

    @Test
    void setBold_doesNotAffectOtherFlags() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setBold(true);
        assertFalse(buffer.getCurrentAttributes().getStyle().getItalic());
        assertFalse(buffer.getCurrentAttributes().getStyle().getUnderline());
    }

    // ==================== setItalic Tests ====================

    @Test
    void setItalic_enablesItalicFlag() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setItalic(true);
        assertTrue(buffer.getCurrentAttributes().getStyle().getItalic());
    }

    @Test
    void setItalic_disablesItalicFlag() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setItalic(true);
        buffer.setItalic(false);
        assertFalse(buffer.getCurrentAttributes().getStyle().getItalic());
    }

    @Test
    void setItalic_doesNotAffectOtherFlags() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setItalic(true);
        assertFalse(buffer.getCurrentAttributes().getStyle().getBold());
        assertFalse(buffer.getCurrentAttributes().getStyle().getUnderline());
    }

    // ==================== setUnderline Tests ====================

    @Test
    void setUnderline_enablesUnderlineFlag() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setUnderline(true);
        assertTrue(buffer.getCurrentAttributes().getStyle().getUnderline());
    }

    @Test
    void setUnderline_disablesUnderlineFlag() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setUnderline(true);
        buffer.setUnderline(false);
        assertFalse(buffer.getCurrentAttributes().getStyle().getUnderline());
    }

    @Test
    void setUnderline_doesNotAffectOtherFlags() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setUnderline(true);
        assertFalse(buffer.getCurrentAttributes().getStyle().getBold());
        assertFalse(buffer.getCurrentAttributes().getStyle().getItalic());
    }

    // ==================== setAttributes Tests ====================

    @Test
    void setAttributes_setsAllAttributesAtOnce() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        CellAttributes attrs = new CellAttributes();
        attrs.setForegroundColor(Color.GREEN);
        attrs.setBackgroundColor(Color.YELLOW);
        attrs.getStyle().setBold(true);
        attrs.getStyle().setItalic(true);
        
        buffer.setAttributes(attrs);
        
        CellAttributes result = buffer.getCurrentAttributes();
        assertEquals(Color.GREEN, result.getForegroundColor());
        assertEquals(Color.YELLOW, result.getBackgroundColor());
        assertTrue(result.getStyle().getBold());
        assertTrue(result.getStyle().getItalic());
    }

    @Test
    void setAttributes_nullThrowsException() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        assertThrows(IllegalArgumentException.class, () -> buffer.setAttributes(null));
    }

    @Test
    void setAttributes_createsDefensiveCopy() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        CellAttributes attrs = new CellAttributes();
        attrs.setForegroundColor(Color.RED);
        buffer.setAttributes(attrs);
        
        attrs.setForegroundColor(Color.BLUE);
        assertEquals(Color.RED, buffer.getCurrentAttributes().getForegroundColor());
    }

    // ==================== getCurrentAttributes Tests ====================

    @Test
    void getCurrentAttributes_returnsDefensiveCopy() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        CellAttributes attrs1 = buffer.getCurrentAttributes();
        CellAttributes attrs2 = buffer.getCurrentAttributes();
        assertNotSame(attrs1, attrs2);
    }

    @Test
    void getCurrentAttributes_modifyingCopyDoesNotAffectBuffer() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        CellAttributes attrs = buffer.getCurrentAttributes();
        attrs.setForegroundColor(Color.RED);
        assertEquals(Color.DEFAULT, buffer.getCurrentAttributes().getForegroundColor());
    }

    // ==================== resetAttributes Tests ====================

    @Test
    void resetAttributes_resetsToDefaults() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setForegroundColor(Color.RED);
        buffer.setBackgroundColor(Color.BLUE);
        buffer.setBold(true);
        buffer.setItalic(true);
        buffer.setUnderline(true);
        
        buffer.resetAttributes();
        
        CellAttributes attrs = buffer.getCurrentAttributes();
        assertEquals(Color.DEFAULT, attrs.getForegroundColor());
        assertEquals(Color.DEFAULT, attrs.getBackgroundColor());
        assertFalse(attrs.getStyle().getBold());
        assertFalse(attrs.getStyle().getItalic());
        assertFalse(attrs.getStyle().getUnderline());
    }

    @Test
    void resetAttributes_canSetAttributesAfterReset() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setForegroundColor(Color.RED);
        buffer.resetAttributes();
        buffer.setForegroundColor(Color.GREEN);
        assertEquals(Color.GREEN, buffer.getCurrentAttributes().getForegroundColor());
    }

    // ==================== getCurrentCursorPosition Tests ====================

    @Test
    void getCursorPosition_returnsDefensiveCopy() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        CursorPosition pos1 = buffer.getCurrentCursorPosition();
        CursorPosition pos2 = buffer.getCurrentCursorPosition();
        assertNotSame(pos1, pos2);
    }

    @Test
    void getCursorPosition_modifyingCopyDoesNotAffectBuffer() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        CursorPosition pos = buffer.getCurrentCursorPosition();
        pos.setColumn(50);
        pos.setRow(10);
        assertEquals(0, buffer.getCurrentCursorPosition().getColumn());
        assertEquals(0, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void getCursorPosition_initialPositionIsZeroZero() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        CursorPosition pos = buffer.getCurrentCursorPosition();
        assertEquals(0, pos.getColumn());
        assertEquals(0, pos.getRow());
    }

    // ==================== setCursorPosition Tests ====================

    @Test
    void setCursorPosition_setsValidPosition() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 5);
        assertEquals(10, buffer.getCurrentCursorPosition().getColumn());
        assertEquals(5, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void setCursorPosition_atOrigin() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 5);
        buffer.setCursorPosition(0, 0);
        assertEquals(0, buffer.getCurrentCursorPosition().getColumn());
        assertEquals(0, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void setCursorPosition_atMaxBounds() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(79, 23);
        assertEquals(79, buffer.getCurrentCursorPosition().getColumn());
        assertEquals(23, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void setCursorPosition_negativeColumnThrowsException() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        assertThrows(IllegalArgumentException.class, () -> buffer.setCursorPosition(-1, 0));
    }

    @Test
    void setCursorPosition_negativeRowThrowsException() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        assertThrows(IllegalArgumentException.class, () -> buffer.setCursorPosition(0, -1));
    }

    @Test
    void setCursorPosition_columnOutOfBoundsThrowsException() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        assertThrows(IllegalArgumentException.class, () -> buffer.setCursorPosition(80, 0));
    }

    @Test
    void setCursorPosition_rowOutOfBoundsThrowsException() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        assertThrows(IllegalArgumentException.class, () -> buffer.setCursorPosition(0, 24));
    }

    // ==================== moveCursorUp Tests ====================

    @Test
    void moveCursorUp_movesUpByN() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 10);
        buffer.moveCursorUp(3);
        assertEquals(10, buffer.getCurrentCursorPosition().getColumn());
        assertEquals(7, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void moveCursorUp_clampsToTopBound() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 5);
        buffer.moveCursorUp(10);
        assertEquals(0, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void moveCursorUp_zeroDoesNotMove() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 10);
        buffer.moveCursorUp(0);
        assertEquals(10, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void moveCursorUp_fromTopRowStaysAtTop() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 0);
        buffer.moveCursorUp(5);
        assertEquals(0, buffer.getCurrentCursorPosition().getRow());
    }

    // ==================== moveCursorDown Tests ====================

    @Test
    void moveCursorDown_movesDownByN() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 10);
        buffer.moveCursorDown(3);
        assertEquals(10, buffer.getCurrentCursorPosition().getColumn());
        assertEquals(13, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void moveCursorDown_clampsToBottomBound() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 20);
        buffer.moveCursorDown(10);
        assertEquals(23, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void moveCursorDown_zeroDoesNotMove() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 10);
        buffer.moveCursorDown(0);
        assertEquals(10, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void moveCursorDown_fromBottomRowStaysAtBottom() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 23);
        buffer.moveCursorDown(5);
        assertEquals(23, buffer.getCurrentCursorPosition().getRow());
    }

    // ==================== moveCursorLeft Tests ====================

    @Test
    void moveCursorLeft_movesLeftByN() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 10);
        buffer.moveCursorLeft(3);
        assertEquals(7, buffer.getCurrentCursorPosition().getColumn());
        assertEquals(10, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void moveCursorLeft_clampsToLeftBound() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(5, 10);
        buffer.moveCursorLeft(10);
        assertEquals(0, buffer.getCurrentCursorPosition().getColumn());
    }

    @Test
    void moveCursorLeft_zeroDoesNotMove() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 10);
        buffer.moveCursorLeft(0);
        assertEquals(10, buffer.getCurrentCursorPosition().getColumn());
    }

    @Test
    void moveCursorLeft_fromLeftColumnStaysAtLeft() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(0, 10);
        buffer.moveCursorLeft(5);
        assertEquals(0, buffer.getCurrentCursorPosition().getColumn());
    }

    // ==================== moveCursorRight Tests ====================

    @Test
    void moveCursorRight_movesRightByN() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 10);
        buffer.moveCursorRight(3);
        assertEquals(13, buffer.getCurrentCursorPosition().getColumn());
        assertEquals(10, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void moveCursorRight_clampsToRightBound() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(75, 10);
        buffer.moveCursorRight(10);
        assertEquals(79, buffer.getCurrentCursorPosition().getColumn());
    }

    @Test
    void moveCursorRight_zeroDoesNotMove() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 10);
        buffer.moveCursorRight(0);
        assertEquals(10, buffer.getCurrentCursorPosition().getColumn());
    }

    @Test
    void moveCursorRight_fromRightColumnStaysAtRight() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(79, 10);
        buffer.moveCursorRight(5);
        assertEquals(79, buffer.getCurrentCursorPosition().getColumn());
    }

    // ==================== writeText Tests ====================

    @Test
    void writeText_writesTextAtCursorPosition() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("Hello");
        assertEquals('H', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals('e', buffer.getScreen().get(0).getCell(1).getCharacter());
        assertEquals('l', buffer.getScreen().get(0).getCell(2).getCharacter());
        assertEquals('l', buffer.getScreen().get(0).getCell(3).getCharacter());
        assertEquals('o', buffer.getScreen().get(0).getCell(4).getCharacter());
    }

    @Test
    void writeText_overridesExistingContent() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("XXXXX");
        buffer.setCursorPosition(0, 0);
        buffer.writeText("Hi");
        assertEquals('H', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals('i', buffer.getScreen().get(0).getCell(1).getCharacter());
        assertEquals('X', buffer.getScreen().get(0).getCell(2).getCharacter());
    }

    @Test
    void writeText_appliesCurrentAttributes() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setForegroundColor(Color.RED);
        buffer.setBold(true);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("A");
        assertEquals(Color.RED, buffer.getScreen().get(0).getCell(0).getAttributes().getForegroundColor());
        assertTrue(buffer.getScreen().get(0).getCell(0).getAttributes().getStyle().getBold());
    }

    @Test
    void writeText_movesCursorRightByTextLength() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("Hello");
        assertEquals(5, buffer.getCurrentCursorPosition().getColumn());
        assertEquals(0, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void writeText_stopsAtEndOfLine() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(7, 0);
        buffer.writeText("Hello");
        // Should only write "Hel" (columns 7, 8, 9) and cursor stops at end
        assertEquals('H', buffer.getScreen().get(0).getCell(7).getCharacter());
        assertEquals('e', buffer.getScreen().get(0).getCell(8).getCharacter());
        assertEquals('l', buffer.getScreen().get(0).getCell(9).getCharacter());
        assertEquals(9, buffer.getCurrentCursorPosition().getColumn());
    }

    @Test
    void writeText_emptyStringDoesNothing() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(5, 5);
        buffer.writeText("");
        assertEquals(5, buffer.getCurrentCursorPosition().getColumn());
        assertEquals(5, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void writeText_nullThrowsException() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        assertThrows(IllegalArgumentException.class, () -> buffer.writeText(null));
    }

    @Test
    void writeText_writesInMiddleOfLine() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(10, 5);
        buffer.writeText("Test");
        assertEquals('T', buffer.getScreen().get(5).getCell(10).getCharacter());
        assertEquals(14, buffer.getCurrentCursorPosition().getColumn());
    }

    @Test
    void writeText_singleCharacter() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("X");
        assertEquals('X', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals(1, buffer.getCurrentCursorPosition().getColumn());
    }

    @Test
    void writeText_fillsEntireLine() {
        TerminalBuffer buffer = new TerminalBuffer(5, 3, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("ABCDE");
        assertEquals('A', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals('E', buffer.getScreen().get(0).getCell(4).getCharacter());
        assertEquals(4, buffer.getCurrentCursorPosition().getColumn());
    }

    // ==================== insertText Tests ====================

    @Test
    void insertText_insertsTextAtCursorPosition() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("World");
        buffer.setCursorPosition(0, 0);
        buffer.insertText("Hello ");
        assertEquals('H', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals('o', buffer.getScreen().get(0).getCell(4).getCharacter());
        assertEquals(' ', buffer.getScreen().get(0).getCell(5).getCharacter());
        assertEquals('W', buffer.getScreen().get(0).getCell(6).getCharacter());
    }

    @Test
    void insertText_shiftsExistingContentRight() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("ABC");
        buffer.setCursorPosition(1, 0);
        buffer.insertText("X");
        assertEquals('A', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals('X', buffer.getScreen().get(0).getCell(1).getCharacter());
        assertEquals('B', buffer.getScreen().get(0).getCell(2).getCharacter());
        assertEquals('C', buffer.getScreen().get(0).getCell(3).getCharacter());
    }

    @Test
    void insertText_appliesCurrentAttributes() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setForegroundColor(Color.GREEN);
        buffer.setItalic(true);
        buffer.setCursorPosition(0, 0);
        buffer.insertText("A");
        assertEquals(Color.GREEN, buffer.getScreen().get(0).getCell(0).getAttributes().getForegroundColor());
        assertTrue(buffer.getScreen().get(0).getCell(0).getAttributes().getStyle().getItalic());
    }

    @Test
    void insertText_movesCursorRightByTextLength() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(0, 0);
        buffer.insertText("Hello");
        assertEquals(5, buffer.getCurrentCursorPosition().getColumn());
    }

    @Test
    void insertText_contentPushedOffLineIsLost() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("ABCDEFGHIJ");  // fills entire line
        buffer.setCursorPosition(0, 0);
        buffer.insertText("XX");
        // "XX" inserted, "AB..HI" shifted, "J" pushed off
        assertEquals('X', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals('X', buffer.getScreen().get(0).getCell(1).getCharacter());
        assertEquals('A', buffer.getScreen().get(0).getCell(2).getCharacter());
        assertEquals('H', buffer.getScreen().get(0).getCell(9).getCharacter());
    }

    @Test
    void insertText_emptyStringDoesNothing() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(5, 5);
        buffer.insertText("");
        assertEquals(5, buffer.getCurrentCursorPosition().getColumn());
    }

    @Test
    void insertText_nullThrowsException() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        assertThrows(IllegalArgumentException.class, () -> buffer.insertText(null));
    }

    @Test
    void insertText_insertsInMiddleOfLine() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("AC");
        buffer.setCursorPosition(1, 0);
        buffer.insertText("B");
        assertEquals('A', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals('B', buffer.getScreen().get(0).getCell(1).getCharacter());
        assertEquals('C', buffer.getScreen().get(0).getCell(2).getCharacter());
    }

    @Test
    void insertText_singleCharacter() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setCursorPosition(0, 0);
        buffer.insertText("X");
        assertEquals('X', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals(1, buffer.getCurrentCursorPosition().getColumn());
    }

    @Test
    void insertText_atEndOfLine() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(9, 0);
        buffer.insertText("X");
        assertEquals('X', buffer.getScreen().get(0).getCell(9).getCharacter());
    }

    // ==================== fillLine(char c) Tests ====================

    @Test
    void fillLine_fillsEntireLineWithCharacter() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(5, 2);
        buffer.fillLine('X');
        for (int i = 0; i < 10; i++) {
            assertEquals('X', buffer.getScreen().get(2).getCell(i).getCharacter());
        }
    }

    @Test
    void fillLine_usesCurrentAttributes() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setForegroundColor(Color.BLUE);
        buffer.setUnderline(true);
        buffer.setCursorPosition(0, 1);
        buffer.fillLine('-');
        assertEquals(Color.BLUE, buffer.getScreen().get(1).getCell(0).getAttributes().getForegroundColor());
        assertTrue(buffer.getScreen().get(1).getCell(0).getAttributes().getStyle().getUnderline());
    }

    @Test
    void fillLine_doesNotMoveCursor() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(3, 2);
        buffer.fillLine('X');
        assertEquals(3, buffer.getCurrentCursorPosition().getColumn());
        assertEquals(2, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void fillLine_fillsCurrentRowOnly() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 2);
        buffer.fillLine('X');
        // Row 2 should be filled
        assertEquals('X', buffer.getScreen().get(2).getCell(0).getCharacter());
        // Other rows should still be spaces
        assertEquals(' ', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals(' ', buffer.getScreen().get(1).getCell(0).getCharacter());
        assertEquals(' ', buffer.getScreen().get(3).getCell(0).getCharacter());
    }

    @Test
    void fillLine_withSpaceCharacter() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("XXXXXXXXXX");
        buffer.fillLine(' ');
        for (int i = 0; i < 10; i++) {
            assertEquals(' ', buffer.getScreen().get(0).getCell(i).getCharacter());
        }
    }

    @Test
    void fillLine_onFirstRow() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(5, 0);
        buffer.fillLine('A');
        assertEquals('A', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals('A', buffer.getScreen().get(0).getCell(9).getCharacter());
    }

    @Test
    void fillLine_onLastRow() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(5, 4);
        buffer.fillLine('Z');
        assertEquals('Z', buffer.getScreen().get(4).getCell(0).getCharacter());
        assertEquals('Z', buffer.getScreen().get(4).getCell(9).getCharacter());
    }

    // ==================== fillLine(char c, int fromColumn, int toColumn) Tests ====================

    @Test
    void fillLineRange_fillsPortionOfLine() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.fillLine('X', 2, 5);
        assertEquals(' ', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals(' ', buffer.getScreen().get(0).getCell(1).getCharacter());
        assertEquals('X', buffer.getScreen().get(0).getCell(2).getCharacter());
        assertEquals('X', buffer.getScreen().get(0).getCell(3).getCharacter());
        assertEquals('X', buffer.getScreen().get(0).getCell(4).getCharacter());
        assertEquals('X', buffer.getScreen().get(0).getCell(5).getCharacter());
        assertEquals(' ', buffer.getScreen().get(0).getCell(6).getCharacter());
    }

    @Test
    void fillLineRange_usesCurrentAttributes() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setForegroundColor(Color.YELLOW);
        buffer.setBold(true);
        buffer.setCursorPosition(0, 1);
        buffer.fillLine('#', 0, 4);
        assertEquals(Color.YELLOW, buffer.getScreen().get(1).getCell(2).getAttributes().getForegroundColor());
        assertTrue(buffer.getScreen().get(1).getCell(2).getAttributes().getStyle().getBold());
    }

    @Test
    void fillLineRange_doesNotMoveCursor() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(3, 2);
        buffer.fillLine('X', 0, 9);
        assertEquals(3, buffer.getCurrentCursorPosition().getColumn());
        assertEquals(2, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void fillLineRange_fromColumnEqualsToColumn() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.fillLine('X', 5, 5);
        assertEquals('X', buffer.getScreen().get(0).getCell(5).getCharacter());
        assertEquals(' ', buffer.getScreen().get(0).getCell(4).getCharacter());
        assertEquals(' ', buffer.getScreen().get(0).getCell(6).getCharacter());
    }

    @Test
    void fillLineRange_entireLine() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.fillLine('Y', 0, 9);
        for (int i = 0; i < 10; i++) {
            assertEquals('Y', buffer.getScreen().get(0).getCell(i).getCharacter());
        }
    }

    @Test
    void fillLineRange_invalidFromColumnThrowsException() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        assertThrows(IllegalArgumentException.class, () -> buffer.fillLine('X', -1, 5));
    }

    @Test
    void fillLineRange_invalidToColumnThrowsException() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        assertThrows(IllegalArgumentException.class, () -> buffer.fillLine('X', 0, 10));
    }

    @Test
    void fillLineRange_fromColumnGreaterThanToColumnThrowsException() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        assertThrows(IllegalArgumentException.class, () -> buffer.fillLine('X', 7, 3));
    }

    @Test
    void fillLineRange_fillsFirstColumn() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(5, 0);
        buffer.fillLine('A', 0, 0);
        assertEquals('A', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals(' ', buffer.getScreen().get(0).getCell(1).getCharacter());
    }

    @Test
    void fillLineRange_fillsLastColumn() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.fillLine('Z', 9, 9);
        assertEquals(' ', buffer.getScreen().get(0).getCell(8).getCharacter());
        assertEquals('Z', buffer.getScreen().get(0).getCell(9).getCharacter());
    }

    @Test
    void fillLineRange_onDifferentRow() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 3);
        buffer.fillLine('M', 2, 7);
        assertEquals('M', buffer.getScreen().get(3).getCell(2).getCharacter());
        assertEquals('M', buffer.getScreen().get(3).getCell(7).getCharacter());
        assertEquals(' ', buffer.getScreen().get(3).getCell(1).getCharacter());
        assertEquals(' ', buffer.getScreen().get(3).getCell(8).getCharacter());
    }
}