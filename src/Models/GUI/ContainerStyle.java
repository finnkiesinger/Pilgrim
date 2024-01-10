package Models.GUI;

import org.joml.Vector4f;

public class ContainerStyle {
    public int borderRadius;
    public int width;
    public int height;
    public Vector4f background;

    public ContainerStyle(int width, int height) {
        this.width = width;
        this.height = height;
        this.borderRadius = 0;
        this.background = new Vector4f();
    }

    public ContainerStyle(int width, int height, int borderRadius, Vector4f background) {
        this.background = background;
        this.borderRadius = borderRadius;
        this.width = width;
        this.height = height;
    }
}
