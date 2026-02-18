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
}