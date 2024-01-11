package Models.GUI;

import org.joml.Vector4f;

public class ContainerStyle {
    public int borderRadius;
    public int width;
    public int height;
    public Vector4f background;

    public ContainerStyle() {
        this.width = -1;
        this.height = -1;
        this.borderRadius = 0;
        this.background = new Vector4f();
    }
}
