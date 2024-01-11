package Models.GUI;

import Models.Size;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Column extends GuiElement {
    List<GuiElement> children;

    public Column() {
        this.children = new ArrayList<>();
    }

    public Size GetSize() {
        Size parentSize = GetParentSize();
        return new Size(parentSize.width, parentSize.height / Math.max(children.size(), 1));
    }

    public void AddChild(GuiElement child) {
        this.children.add(child);
        child.parent = this;
    }

    public void Render(Vector3f offset) {
        Size parentSize = GetParentSize();

        float accu = 0.0f;
        for (int i = 0; i < children.size(); i++) {
            GuiElement child = children.get(i);

            float height = Math.min(child.GetSize().height, GetSize().height);
            accu += height;
            float position = parentSize.height - accu;
            child.Render(new Vector3f(0.0f, offset.y + position, 0.0f));
        }
    }
}
