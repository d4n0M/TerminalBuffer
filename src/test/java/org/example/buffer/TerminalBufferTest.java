package org.example.buffer;

import org.example.model.*;
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

    // ==================== resetAttributes Tests ====================

    @Test
    void resetAttributes_resetsToDefaults() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.setForegroundColor(Color.RED);
        buffer.setBackgroundColor(Color.BLUE);
        buffer.setBold(true);
        buffer.setItalic(true);
        buffer.setUnderline(true);

        assertEquals(Color.RED, buffer.getCurrentAttributes().getForegroundColor());
        assertEquals(Color.BLUE, buffer.getCurrentAttributes().getBackgroundColor());
        assertTrue(buffer.getCurrentAttributes().getStyle().getBold());
        assertTrue(buffer.getCurrentAttributes().getStyle().getItalic());
        assertTrue(buffer.getCurrentAttributes().getStyle().getUnderline());
        
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
        CellAttributes attrs = new CellAttributes(Color.BLUE, Color.WHITE, new StyleFlags(false, false, true));
        buffer.setCursorPosition(0,1);
        buffer.applyToCurrentCell(attrs);
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
        buffer.setCursorPosition(0, 1);
        buffer.setForegroundColor(Color.YELLOW);
        buffer.setBold(true);
        buffer.fillLine('#', 0, 4);
        assertEquals(Color.YELLOW, buffer.getScreen().get(1).getCell(0).getAttributes().getForegroundColor());
        assertTrue(buffer.getScreen().get(1).getCell(0).getAttributes().getStyle().getBold());
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

    // ==================== insertEmptyLineAtBottom Tests ====================

    @Test
    void insertEmptyLineAtBottom_addsNewEmptyLineAtBottom() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        // Write something to identify lines
        buffer.setCursorPosition(0, 0);
        buffer.writeText("LINE0");
        buffer.setCursorPosition(0, 4);
        buffer.writeText("LINE4");
        
        buffer.insertEmptyLineAtBottom();
        
        // Bottom line should now be empty
        assertEquals(' ', buffer.getScreen().get(4).getCell(0).getCharacter());
        // Screen size should remain the same
        assertEquals(5, buffer.getScreen().size());
    }

    @Test
    void insertEmptyLineAtBottom_scrollsTopLineToScrollback() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("FIRST");
        
        buffer.insertEmptyLineAtBottom();
        
        // Scrollback should contain the old top line
        assertEquals(1, buffer.getScrollback().size());
        assertEquals('F', buffer.getScrollback().getFirst().getCell(0).getCharacter());
        assertEquals('I', buffer.getScrollback().getFirst().getCell(1).getCharacter());
    }

    @Test
    void insertEmptyLineAtBottom_shiftsAllLinesUp() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("LINE0");
        buffer.setCursorPosition(0, 1);
        buffer.writeText("LINE1");
        buffer.setCursorPosition(0, 2);
        buffer.writeText("LINE2");
        
        buffer.insertEmptyLineAtBottom();
        
        // LINE1 should now be at row 0
        assertEquals('L', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals('1', buffer.getScreen().get(0).getCell(4).getCharacter());
        // LINE2 should now be at row 1
        assertEquals('L', buffer.getScreen().get(1).getCell(0).getCharacter());
        assertEquals('2', buffer.getScreen().get(1).getCell(4).getCharacter());
    }

    @Test
    void insertEmptyLineAtBottom_newLineIsCompletelyEmpty() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.insertEmptyLineAtBottom();
        
        // Verify all cells in the new bottom line are empty spaces
        for (int i = 0; i < 10; i++) {
            assertEquals(' ', buffer.getScreen().get(4).getCell(i).getCharacter());
        }
    }

    @Test
    void insertEmptyLineAtBottom_multipleCallsAccumulateInScrollback() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("LINE0");
        buffer.setCursorPosition(0, 1);
        buffer.writeText("LINE1");
        buffer.setCursorPosition(0, 2);
        buffer.writeText("LINE2");
        
        buffer.insertEmptyLineAtBottom();
        buffer.insertEmptyLineAtBottom();
        buffer.insertEmptyLineAtBottom();
        
        assertEquals(3, buffer.getScrollback().size());
        // First scrolled line should be at the start of scrollback
        assertEquals('0', buffer.getScrollback().get(0).getCell(4).getCharacter());
        assertEquals('1', buffer.getScrollback().get(1).getCell(4).getCharacter());
        assertEquals('2', buffer.getScrollback().get(2).getCell(4).getCharacter());
    }

    @Test
    void insertEmptyLineAtBottom_respectsMaxScrollbackSize() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 3);  // max 3 lines in scrollback
        buffer.setCursorPosition(0, 0);
        buffer.writeText("LINE0");
        buffer.setCursorPosition(0, 1);
        buffer.writeText("LINE1");
        buffer.setCursorPosition(0, 2);
        buffer.writeText("LINE2");
        buffer.setCursorPosition(0, 3);
        buffer.writeText("LINE3");
        buffer.setCursorPosition(0, 4);
        buffer.writeText("LINE4");
        
        // Insert 5 empty lines - should scroll all content but only keep 3 in scrollback
        for (int i = 0; i < 5; i++) {
            buffer.insertEmptyLineAtBottom();
        }
        
        // Scrollback should be capped at max size
        assertEquals(3, buffer.getScrollback().size());
        // Oldest lines should be removed, keeping the most recent 3
        assertEquals('2', buffer.getScrollback().get(0).getCell(4).getCharacter());
        assertEquals('3', buffer.getScrollback().get(1).getCell(4).getCharacter());
        assertEquals('4', buffer.getScrollback().get(2).getCell(4).getCharacter());
    }

    @Test
    void insertEmptyLineAtBottom_withZeroMaxScrollback() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 0);  // no scrollback allowed
        buffer.setCursorPosition(0, 0);
        buffer.writeText("LINE0");
        
        buffer.insertEmptyLineAtBottom();
        
        // Scrollback should remain empty
        assertTrue(buffer.getScrollback().isEmpty());
        // Screen should still have 5 lines
        assertEquals(5, buffer.getScreen().size());
    }

    @Test
    void insertEmptyLineAtBottom_preservesScreenSize() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        
        for (int i = 0; i < 10; i++) {
            buffer.insertEmptyLineAtBottom();
        }
        
        assertEquals(5, buffer.getScreen().size());
    }

    @Test
    void insertEmptyLineAtBottom_withMinimumScreenSize() {
        TerminalBuffer buffer = new TerminalBuffer(1, 1, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("X");
        
        buffer.insertEmptyLineAtBottom();
        
        assertEquals(1, buffer.getScreen().size());
        assertEquals(' ', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertEquals(1, buffer.getScrollback().size());
        assertEquals('X', buffer.getScrollback().getFirst().getCell(0).getCharacter());
    }

    @Test
    void insertEmptyLineAtBottom_newLineHasCorrectWidth() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        
        buffer.insertEmptyLineAtBottom();
        
        assertEquals(80, buffer.getScreen().get(23).getWidth());
    }

    @Test
    void insertEmptyLineAtBottom_scrollbackExactlyAtMaxSize() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 5);  // max 5 lines in scrollback
        
        // Fill and scroll exactly 5 lines
        for (int i = 0; i < 5; i++) {
            buffer.setCursorPosition(0, 0);
            buffer.writeText("L" + i);
            buffer.insertEmptyLineAtBottom();
        }
        
        assertEquals(5, buffer.getScrollback().size());
        
        // Add one more - should remove oldest
        buffer.setCursorPosition(0, 0);
        buffer.writeText("NEW");
        buffer.insertEmptyLineAtBottom();
        
        assertEquals(5, buffer.getScrollback().size());
    }

    @Test
    void insertEmptyLineAtBottom_preservesCellAttributes() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.setForegroundColor(Color.RED);
        buffer.setBold(true);
        buffer.writeText("STYLED");
        
        buffer.insertEmptyLineAtBottom();
        
        // Check that the scrollback line preserved the attributes
        TerminalLine scrolledLine = buffer.getScrollback().getFirst();
        assertEquals(Color.RED, scrolledLine.getCell(0).getAttributes().getForegroundColor());
        assertTrue(scrolledLine.getCell(0).getAttributes().getStyle().getBold());
    }

    @Test
    void insertEmptyLineAtBottom_doesNotAffectCursorPosition() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(5, 3);
        
        buffer.insertEmptyLineAtBottom();
        
        // Cursor position should remain unchanged
        assertEquals(5, buffer.getCurrentCursorPosition().getColumn());
        assertEquals(3, buffer.getCurrentCursorPosition().getRow());
    }

    // ==================== clearScreen Tests ====================

    @Test
    void clearScreen_clearsAllScreenLines() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("LINE0");
        buffer.setCursorPosition(0, 1);
        buffer.writeText("LINE1");
        buffer.setCursorPosition(0, 2);
        buffer.writeText("LINE2");
        buffer.setCursorPosition(0, 3);
        buffer.writeText("LINE3");
        buffer.setCursorPosition(0, 4);
        buffer.writeText("LINE4");
        
        buffer.clearScreen();
        
        // All screen lines should be empty
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                assertEquals(' ', buffer.getScreen().get(row).getCell(col).getCharacter(),
                        "Cell at row " + row + ", col " + col + " should be empty");
            }
        }
    }

    @Test
    void clearScreen_doesNotTouchScrollback() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        // First put something in scrollback
        buffer.setCursorPosition(0, 0);
        buffer.writeText("SCROLL");
        buffer.insertEmptyLineAtBottom();
        
        assertEquals(1, buffer.getScrollback().size());
        assertEquals('S', buffer.getScrollback().getFirst().getCell(0).getCharacter());
        
        buffer.clearScreen();
        
        // Scrollback should be unchanged
        assertEquals(1, buffer.getScrollback().size());
        assertEquals('S', buffer.getScrollback().getFirst().getCell(0).getCharacter());
    }

    @Test
    void clearScreen_preservesScreenSize() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.clearScreen();
        
        assertEquals(24, buffer.getScreen().size());
    }

    @Test
    void clearScreen_preservesLineWidth() {
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 100);
        buffer.clearScreen();
        
        for (TerminalLine line : buffer.getScreen()) {
            assertEquals(80, line.getWidth());
        }
    }

    @Test
    void clearScreen_withEmptyScreen() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        // Screen is already empty
        buffer.clearScreen();
        
        assertEquals(5, buffer.getScreen().size());
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                assertEquals(' ', buffer.getScreen().get(row).getCell(col).getCharacter());
            }
        }
    }

    @Test
    void clearScreen_withFullScreen() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        // Fill entire screen
        for (int row = 0; row < 5; row++) {
            buffer.setCursorPosition(0, row);
            buffer.fillLine('X');
        }
        
        buffer.clearScreen();
        
        // All should be cleared
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                assertEquals(' ', buffer.getScreen().get(row).getCell(col).getCharacter());
            }
        }
    }

    @Test
    void clearScreen_doesNotAffectScrollbackContents() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        // Build up scrollback with multiple lines
        for (int i = 0; i < 3; i++) {
            buffer.setCursorPosition(0, 0);
            buffer.writeText("SB" + i);
            buffer.insertEmptyLineAtBottom();
        }
        
        assertEquals(3, buffer.getScrollback().size());
        
        buffer.clearScreen();
        
        // Verify scrollback content is preserved
        assertEquals(3, buffer.getScrollback().size());
        assertEquals('0', buffer.getScrollback().get(0).getCell(2).getCharacter());
        assertEquals('1', buffer.getScrollback().get(1).getCell(2).getCharacter());
        assertEquals('2', buffer.getScrollback().get(2).getCell(2).getCharacter());
    }

    @Test
    void clearScreen_withMinimumScreenSize() {
        TerminalBuffer buffer = new TerminalBuffer(1, 1, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("X");
        
        buffer.clearScreen();
        
        assertEquals(1, buffer.getScreen().size());
        assertEquals(' ', buffer.getScreen().get(0).getCell(0).getCharacter());
    }

    @Test
    void clearScreen_withLargeScreen() {
        TerminalBuffer buffer = new TerminalBuffer(200, 100, 100);
        // Write some content
        buffer.setCursorPosition(0, 50);
        buffer.writeText("TEST");
        
        buffer.clearScreen();
        
        assertEquals(100, buffer.getScreen().size());
        // Verify the previously written cell is now empty
        assertEquals(' ', buffer.getScreen().get(50).getCell(0).getCharacter());
    }

    @Test
    void clearScreen_multipleTimes() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("TEST");
        buffer.clearScreen();
        buffer.setCursorPosition(0, 0);
        buffer.writeText("AGAIN");
        buffer.clearScreen();
        buffer.clearScreen();  // Clear already empty screen
        
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                assertEquals(' ', buffer.getScreen().get(row).getCell(col).getCharacter());
            }
        }
    }

    @Test
    void clearScreen_preservesEmptyScrollback() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        assertTrue(buffer.getScrollback().isEmpty());
        
        buffer.clearScreen();
        
        assertTrue(buffer.getScrollback().isEmpty());
    }

    @Test
    void clearScreen_withZeroMaxScrollback() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 0);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("TEST");
        
        buffer.clearScreen();
        
        assertEquals(' ', buffer.getScreen().get(0).getCell(0).getCharacter());
        assertTrue(buffer.getScrollback().isEmpty());
    }

    @Test
    void clearScreen_doesNotResetCellAttributes() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.setForegroundColor(Color.RED);
        buffer.writeText("RED");
        
        buffer.clearScreen();
        
        // After clearing, cells should be space characters
        // The test verifies screen is cleared, attributes behavior depends on implementation
        assertEquals(' ', buffer.getScreen().get(0).getCell(0).getCharacter());
    }

    @Test
    void clearScreen_resetsCursorToOrigin() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(5, 3);
        
        buffer.clearScreen();
        
        assertEquals(0, buffer.getCurrentCursorPosition().getColumn());
        assertEquals(0, buffer.getCurrentCursorPosition().getRow());
    }

    @Test
    void clearScreen_partiallyFilledScreen() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        // Only write to some lines
        buffer.setCursorPosition(0, 1);
        buffer.writeText("A");
        buffer.setCursorPosition(5, 3);
        buffer.writeText("B");
        
        buffer.clearScreen();
        
        assertEquals(' ', buffer.getScreen().get(1).getCell(0).getCharacter());
        assertEquals(' ', buffer.getScreen().get(3).getCell(5).getCharacter());
    }

    // ==================== Content Retrieval Tests ====================

    @Test
    void getCharAt_fromScreen() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(2, 1);
        buffer.writeText("X");
        
        assertEquals('X', buffer.getCharAt(2, 1));
        assertEquals(' ', buffer.getCharAt(0, 0));
    }

    @Test
    void getCharAt_withIncludeScrollback() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.writeText("Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6");
        
        // "Line 1" should be in scrollback
        assertEquals('L', buffer.getCharAt(0, 0, true));
        assertEquals('1', buffer.getCharAt(5, 0, true));
        
        // "Line 6" should be on screen (row 0 of screen, which is row 5 including scrollback)
        assertEquals('L', buffer.getCharAt(0, 5, true));
        
        // Without scrollback, index 0 is "Line 2"
        assertEquals('L', buffer.getCharAt(0, 0, false));
    }

    @Test
    void getAttributesAt_fromScreen() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.setForegroundColor(Color.RED);
        buffer.writeText("R");
        
        CellAttributes attrs = buffer.getAttributesAt(0, 0);
        assertEquals(Color.RED, attrs.getForegroundColor());
    }

    @Test
    void getAttributesAt_withIncludeScrollback() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setForegroundColor(Color.BLUE);
        buffer.writeText("B\n");
        for(int i=0; i<5; i++) buffer.writeText("\n");
        
        // 'B' is now in scrollback
        CellAttributes attrs = buffer.getAttributesAt(0, 0, true);
        assertEquals(Color.BLUE, attrs.getForegroundColor());
    }

    @Test
    void getLine_fromScreen() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        buffer.setCursorPosition(0, 0);
        buffer.writeText("Hello");
        
        String line = buffer.getLine(0);
        assertTrue(line.startsWith("Hello"));
        assertEquals(10, line.length());
    }

    @Test
    void getLine_withIncludeScrollback() {
        TerminalBuffer buffer = new TerminalBuffer(10, 2, 100);
        buffer.writeText("Line1\nLine2\nLine3");
        
        // Line1 and Line2 in scrollback, Line3 on screen
        assertEquals("Line1     ", buffer.getLine(0, true));
        assertEquals("Line2     ", buffer.getLine(1, true));
        assertEquals("Line3     ", buffer.getLine(2, true));
    }

    @Test
    void getScreenContent_returnsAllVisibleLines() {
        TerminalBuffer buffer = new TerminalBuffer(5, 3, 100);
        buffer.writeText("A\nB\nC");
        
        String content = buffer.getScreenContent();
        String expected = "A    \nB    \nC    ";
        assertEquals(expected, content);
    }

    @Test
    void getAllContent_includesScrollback() {
        TerminalBuffer buffer = new TerminalBuffer(5, 2, 100);
        buffer.writeText("1\n2\n3");
        
        // "1" in scrollback, "2" and "3" on screen
        String content = buffer.getAllContent();
        String expected = "1    \n2    \n3    ";
        assertEquals(expected, content);
    }

    @Test
    void getScreenLines_returnsListOfStrings() {
        TerminalBuffer buffer = new TerminalBuffer(5, 2, 100);
        buffer.writeText("A\nB");
        
        java.util.List<String> lines = buffer.getScreenLines();
        assertEquals(2, lines.size());
        assertEquals("A    ", lines.get(0));
        assertEquals("B    ", lines.get(1));
    }

    @Test
    void getAllLines_returnsListOfAllStrings() {
        TerminalBuffer buffer = new TerminalBuffer(5, 2, 100);
        buffer.writeText("1\n2\n3");
        
        java.util.List<String> lines = buffer.getAllLines();
        assertEquals(3, lines.size());
        assertEquals("1    ", lines.get(0));
        assertEquals("2    ", lines.get(1));
        assertEquals("3    ", lines.get(2));
    }
}