package Models.GUI;

import org.joml.Vector4f;

public class Color {
    public int red;
    public int green;
    public int blue;
    public int alpha;

    public float r() {
        return 1.0f / red;
    }

    public float g() {
        return 1.0f / green;
    }

    public float b() {
        return 1.0f / blue;
    }

    public float a() {
        return 1.0f / alpha;
    }

    public Color(int r, int g, int b) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = 255;
    }

    public Color(int r, int g, int b, int a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }

    public Color(float r, float g, float b) {
        this.red = (int) r * 255;
        this.green = (int) g * 255;
        this.blue = (int) b * 255;
    }

    public Color(float r, float g, float b, float a) {
        this.red = (int) r * 255;
        this.green = (int) g * 255;
        this.blue = (int) b * 255;
        this.alpha = (int) a * 255;
    }

    public Vector4f getColor() {
        return new Vector4f(r(), g(), b(), a());
    }
}
