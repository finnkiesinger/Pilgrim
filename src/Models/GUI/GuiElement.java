package Models.GUI;

import Models.Size;
import Window.Window;
import org.joml.Vector3f;

public abstract class GuiElement {
    protected  int VAO;
    public Vector3f position;
    protected GuiElement parent;

    abstract protected Size GetSize();

    protected void SetParent(GuiElement parent) {
        this.parent = parent;
    }

    protected Size GetParentSize() {
        if (this.parent != null) {
            return parent.GetSize();
        }

        return new Size(Window.ActiveWindow().GetWidth(), Window.ActiveWindow().GetHeight());
    }

    abstract public void Render(Vector3f offset);
}
