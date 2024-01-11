package Models.GUI;

import Models.Size;
import Window.Window;
import org.joml.Vector3f;

public class Center extends GuiElement {
    private final GuiElement child;

    protected Size GetSize() {
        return GetParentSize();
    }

    public Center(GuiElement child) {
        this.child = child;
        this.child.SetParent(this);
    }

    public void Render(Vector3f offset) {
        Size size = GetSize();
        Size childSize = child.GetSize();
        Vector3f center = new Vector3f((size.width - childSize.width) / 2.0f, (size.height - childSize.height) / 2.0f, 0.0f);
        this.child.Render(center.add(offset));
    }
}
