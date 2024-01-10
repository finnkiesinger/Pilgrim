package Models.GUI;

import org.joml.Vector4f;

public class TextStyle {
    private int fontSize;
    private Vector4f color;

    public TextStyle(int fontSize, Vector4f color) {
        this.fontSize = fontSize;
        this.color = color;
    }

    public TextStyle() {
        this.fontSize = 16;
        this.color = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public int GetFontSize() {
        return this.fontSize;
    }

    public Vector4f GetColor() {
        return this.color;
    }
}
