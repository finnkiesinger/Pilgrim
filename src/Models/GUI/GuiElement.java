package Models.GUI;

import Models.Size;
import org.joml.Vector3f;

public abstract class GuiElement {
    protected  int VAO;
    public Vector3f position;
    protected GuiElement parent;

    abstract protected Size GetSize();

    protected void SetParent(GuiElement parent) {
        this.parent = parent;
    }

    abstract public void Render(Vector3f offset);
}
