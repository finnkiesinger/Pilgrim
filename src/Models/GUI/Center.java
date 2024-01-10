package Models.GUI;

import Models.Size;
import org.joml.Vector3f;

public class Center extends GuiElement {
    private final GuiElement child;

    protected void CalculateSize() {
        if (parent == null) {
            this.size = child.size;
            return;
        }

        if (parent.size == Size.AUTOMATIC) {
            this.size = child.size;
        } else {
            this.size = parent.size;
        }
    }

    public Center(GuiElement child) {
        this.child = child;
        this.child.SetParent(this);
        CalculateSize();
    }

    public void Render(Vector3f offset) {
        Vector3f center = new Vector3f((size.width - child.size.width) / 2.0f, (size.height - child.size.height) / 2.0f, 0.0f);
        this.child.Render(center.add(offset));
    }
}
