package org.example.buffer;

import org.example.model.CellAttributes;
import org.example.model.Color;
import org.example.model.CursorPosition;
import org.example.model.StyleFlags;

import java.util.ArrayList;
import java.util.LinkedList;

public class TerminalBuffer {
    private int width;
    private int height;
    private int maxScrollbackLines;
    private ArrayList<TerminalLine> screen;
    private LinkedList<TerminalLine> scrollback;
    CursorPosition cursor;
    CellAttributes currentAttributes;

    public TerminalBuffer(int width, int height, int maxScrollbackLines) {
        this.width = width;
        this.height = height;
        this.maxScrollbackLines = maxScrollbackLines;

        this.screen = new ArrayList<>(height);
        initializeScreen();
        
        this.scrollback = new LinkedList<>();
        this.cursor = new CursorPosition(0, 0);
        this.currentAttributes = new CellAttributes();
    }

    private void initializeScreen() {
        for (int i = 0; i < height; i++) {
            screen.add(createEmptyLine());
        }
    }

    private TerminalLine createEmptyLine() {
        return new TerminalLine(width);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxScrollbackLines() {
        return maxScrollbackLines;
    }

    public ArrayList<TerminalLine> getScreen() {
        return screen;
    }

    public LinkedList<TerminalLine> getScrollback() {
        return scrollback;
    }

    public CursorPosition getCursor() {
        return cursor;
    }

    /**
     * Gets a copy of the current attributes.
     * @return a defensive copy of the current cell attributes
     */
    public CellAttributes getCurrentAttributes() {
        return new CellAttributes(currentAttributes);
    }

    /**
     * Sets the foreground color for future writes.
     * @param color the foreground color to set
     * @throws IllegalArgumentException if color is null
     */
    public void setForegroundColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        currentAttributes.setForegroundColor(color);
    }

    /**
     * Sets the background color for future writes.
     * @param color the background color to set
     * @throws IllegalArgumentException if color is null
     */
    public void setBackgroundColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        currentAttributes.setBackgroundColor(color);
    }

    /**
     * Sets the bold flag for future writes.
     * @param bold true to enable bold, false to disable
     */
    public void setBold(boolean bold) {
        currentAttributes.getStyle().setBold(bold);
    }

    /**
     * Sets the italic flag for future writes.
     * @param italic true to enable italic, false to disable
     */
    public void setItalic(boolean italic) {
        currentAttributes.getStyle().setItalic(italic);
    }

    /**
     * Sets the underline flag for future writes.
     * @param underline true to enable underline, false to disable
     */
    public void setUnderline(boolean underline) {
        currentAttributes.getStyle().setUnderline(underline);
    }

    /**
     * Sets all attributes at once.
     * @param attrs the attributes to set
     * @throws IllegalArgumentException if attrs is null
     */
    public void setAttributes(CellAttributes attrs) {
        if (attrs == null) {
            throw new IllegalArgumentException("Attributes cannot be null");
        }
        currentAttributes.setForegroundColor(attrs.getForegroundColor());
        currentAttributes.setBackgroundColor(attrs.getBackgroundColor());
        currentAttributes.setStyle(new StyleFlags(attrs.getStyle()));
    }

    /**
     * Resets attributes to defaults.
     */
    public void resetAttributes() {
        currentAttributes.reset();
    }
}
