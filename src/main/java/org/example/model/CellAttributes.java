package org.example.model;

public class CellAttributes {
    private Color foregroundColor;
    private Color backgroundColor;
    private StyleFlags style;

    public CellAttributes(Color foregroundColor, Color backgroundColor, StyleFlags style){
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.style = new StyleFlags(style);
    }

    public CellAttributes(){
        this(Color.DEFAULT, Color.DEFAULT, new StyleFlags());
    }

    public CellAttributes(CellAttributes attributes){
        this(attributes.foregroundColor, attributes.backgroundColor, new StyleFlags(attributes.style));
    }

    public void reset(){
        foregroundColor = Color.DEFAULT;
        backgroundColor = Color.DEFAULT;
        style.reset();
    }

    public void applyToCell(Cell cell){
        cell.setForegroundColor(foregroundColor);
        cell.setBackgroundColor(backgroundColor);
        cell.setStyle(new StyleFlags(style));
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public StyleFlags getStyle() {
        return style;
    }

    public void setStyle(StyleFlags style) {
        this.style = new StyleFlags(style);
    }
}