package Models.GUI;

import Models.Size;
import org.joml.Vector3f;

public abstract class GuiElement {
    protected  int VAO;
    public Vector3f position;
    protected GuiElement parent;

    protected Size size = Size.AUTOMATIC;

    abstract protected void CalculateSize();

    protected void SetParent(GuiElement parent) {
        this.parent = parent;
        parent.CalculateSize();
        CalculateSize();
    }

    abstract public void Render(Vector3f offset);
}
