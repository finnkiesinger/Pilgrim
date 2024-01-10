package Models.GUI;

import Models.Size;
import org.joml.Vector3f;

public class Positioned extends GuiElement {
    public float x;
    public float y;
    public float width;
    public float height;

    public GuiElement child;

    public Positioned(GuiElement child) {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.child = child;
        child.SetParent(this);
    }

    public Size GetSize() {
        Size size = new Size(0, 0);
        if (width != 0) {
            size.width = this.width;
        }
        if (height != 0) {
            size.height = this.height;
        }
        return size;
    }

    public void Render(Vector3f offset) {
        child.Render(new Vector3f(x, y, 0.0f));
    }
}
