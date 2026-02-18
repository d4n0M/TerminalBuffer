package org.example.buffer;

import org.example.model.CellAttributes;
import org.example.model.CursorPosition;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class TerminalBuffer {
    private int width;
    private int height;
    private int maxScrollbackLines;
    private List<TerminalLine> screen;
    private Deque<TerminalLine> scrollback;
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

    public List<TerminalLine> getScreen() {
        return screen;
    }

    public Deque<TerminalLine> getScrollback() {
        return scrollback;
    }

    public CursorPosition getCursor() {
        return cursor;
    }

    public CellAttributes getCurrentAttributes() {
        return currentAttributes;
    }
}
