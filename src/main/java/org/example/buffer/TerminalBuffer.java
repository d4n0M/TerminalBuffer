package org.example.buffer;

import org.example.model.*;

import java.util.ArrayList;
import java.util.LinkedList;

public class TerminalBuffer {
    private int width;
    private int height;
    private int maxScrollbackLines;
    private ArrayList<TerminalLine> screen;
    private LinkedList<TerminalLine> scrollback;
    CursorPosition cursor;

    public TerminalBuffer(int width, int height, int maxScrollbackLines) {
        this.width = width;
        this.height = height;
        this.maxScrollbackLines = maxScrollbackLines;

        this.screen = new ArrayList<>(height);
        initializeScreen();
        
        this.scrollback = new LinkedList<>();
        this.cursor = new CursorPosition(0, 0);
    }

    private void initializeScreen() {
        for (int i = 0; i < height; i++) {
            screen.add(createEmptyLine());
        }
    }

    private TerminalLine createEmptyLine() {
        return new TerminalLine(width);
    }

    public CellAttributes getCurrentAttributes(){
        return screen.get(cursor.getRow()).getCell(cursor.getColumn()).getAttributes();
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

    public void applyToCurrentCell(CellAttributes attrs){
        screen.get(cursor.getRow()).getCell(cursor.getColumn()).setAttributes(attrs);
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

        getCurrentAttributes().setForegroundColor(color);
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
        getCurrentAttributes().setBackgroundColor(color);
    }

    /**
     * Sets the bold flag for future writes.
     * @param bold true to enable bold, false to disable
     */
    public void setBold(boolean bold) {
        getCurrentAttributes().getStyle().setBold(bold);
    }

    /**
     * Sets the italic flag for future writes.
     * @param italic true to enable italic, false to disable
     */
    public void setItalic(boolean italic) {
        getCurrentAttributes().getStyle().setItalic(italic);
    }

    /**
     * Sets the underline flag for future writes.
     * @param underline true to enable underline, false to disable
     */
    public void setUnderline(boolean underline) {
        getCurrentAttributes().getStyle().setUnderline(underline);
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
        setForegroundColor(attrs.getForegroundColor());
        setBackgroundColor(attrs.getBackgroundColor());
        getCurrentAttributes().setStyle(new StyleFlags(attrs.getStyle()));
    }

    /**
     * Resets attributes to defaults.
     */
    public void resetAttributes() {
        getCurrentAttributes().reset();
    }

    private void clampCursorToBounds(){
        if (cursor.getRow() < 0)  {
            cursor.setRow(0);
        }else if (cursor.getRow() >= height-1) {
            cursor.setRow(height-1);
        }
        if (cursor.getColumn() < 0)  {
            cursor.setColumn(0);
        }else if (cursor.getColumn() >= width-1) {
            cursor.setColumn(width-1);
        }
    }

    private boolean validatePosition(int column, int row){
        return row >= 0 && row < height && column >= 0 && column < width;
    }

    public CursorPosition getCurrentCursorPosition(){
        return new CursorPosition(cursor.getColumn(), cursor.getRow());
    }

    public void setCursorPosition(int  column, int row){
        if (validatePosition(column, row)){
            cursor.setColumn(column);
            cursor.setRow(row);
            return;
        }
        throw new IllegalArgumentException("Invalid cursor position");
    }

    public void moveCursorUp(int n){
        cursor.setRow(cursor.getRow() - n);
        clampCursorToBounds();
    }

    public void moveCursorDown(int n){
        cursor.setRow(cursor.getRow() + n);
        clampCursorToBounds();
    }

    public void moveCursorLeft(int n){
        cursor.setColumn(cursor.getColumn() - n);
        clampCursorToBounds();
    }

    public void moveCursorRight(int n){
        cursor.setColumn(cursor.getColumn() + n);
        clampCursorToBounds();
    }

    private TerminalLine getCurrentLine(){
        return screen.get(cursor.getRow());
    }

    /**
     * Inserts a character at the current cursor position, shifting existing content right.
     * If content is pushed off the end of the line, it wraps to the next line recursively.
     * Preserves the attributes of wrapped characters.
     *
     * @param line the line to insert into
     * @param c the character to insert
     */
    private void insertAndShift(TerminalLine line, char c) {
        int col = cursor.getColumn();
        int row = cursor.getRow();

        // Save the cell that will fall off the right edge (if any)
        Cell overflowCell = new Cell(line.getCell(width - 1));

        CellAttributes currentCellAttributes = getCurrentAttributes();

        // CurrentAttributes.style is already a new instance of StyleAttributes
        Cell newCell = new Cell(c, currentCellAttributes.getForegroundColor(),
                currentCellAttributes.getBackgroundColor(), currentCellAttributes.getStyle());

        // Shift all cells to the right, starting from the end
        for (int i = width - 1; i > col; i--) {
            line.setCell(i, new Cell(line.getCell(i - 1)));
        }

        // Insert the new cell at the cursor position
        line.setCell(col, newCell);

        // Handle wrapping if we pushed a non-empty cell off the edge
        if (!overflowCell.isEmpty() && row < height - 1) {
            // Save current state
            int originalCol = cursor.getColumn();
            int originalRow = cursor.getRow();
            CellAttributes originalAttrs = new CellAttributes(getCurrentAttributes());

            // Set cursor to the beginning of the next line
            cursor.setColumn(0);
            cursor.setRow(row + 1);

            // Set attributes to match the overflow cell (to preserve its formatting)
            CellAttributes currentAttributes = getCurrentAttributes();
            currentAttributes.setForegroundColor(overflowCell.getForegroundColor());
            currentAttributes.setBackgroundColor(overflowCell.getBackgroundColor());
            currentAttributes.setStyle(overflowCell.getStyle());

            // Recursively insert the overflow character into the next line
            TerminalLine nextLine = screen.get(row + 1);
            insertAndShift(nextLine, overflowCell.getCharacter());

            // Restore the original cursor position and attributes
            cursor.setColumn(originalCol);
            cursor.setRow(originalRow);
            currentAttributes = originalAttrs;
        }

        // Move cursor right after insertion
        moveCursorRight(1);
    }

    /**
     * Inserts text at the current cursor position.
     * Each character is inserted using insertAndShift, which may cause line wrapping.
     * The cursor moves right as characters are inserted.
     *
     * @param text the text to insert
     */
    public void insertText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }

        for (char c : text.toCharArray()) {
            TerminalLine line = getCurrentLine();
            insertAndShift(line, c);
        }
    }

    public void writeText(String text){
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }

        TerminalLine line = getCurrentLine();
        boolean breakAtEnd = false;
        for(char c : text.toCharArray()){
            if(cursor.getColumn() == width-1){
                breakAtEnd = true;
            }
            Cell cell = line.getCell(cursor.getColumn());
            cell.setCharacter(c);
            moveCursorRight(1);
            if(breakAtEnd){ break; }
        }
    }

    public void fillLine(char c){
        TerminalLine line = getCurrentLine();
        for(Cell cell : line.getCells()){
            cell.setCharacter(c);
        }
    }

    //user is responsible for index checking
    public void fillLine(char c, int from, int to){

        if(!(from >= 0 && from <= to && from < width && to < width)){
            throw new IllegalArgumentException("Invalid bounds.");
        }

        TerminalLine line = getCurrentLine();
        for(int i = from; i <= to; i++){
            Cell cell = line.getCell(i);
            cell.setCharacter(c);
        }
    }
}

