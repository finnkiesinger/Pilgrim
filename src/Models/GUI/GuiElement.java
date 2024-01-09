package Models.GUI;

import org.joml.Vector3f;

public abstract class GuiElement {
    protected  int VAO;
    public Vector3f position;

    abstract public void Render();
}
