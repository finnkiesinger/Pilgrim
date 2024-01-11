package Models.GUI;

import Models.Size;
import org.joml.Vector3f;

public class ScrollView extends GuiElement {
    public Size GetSize() {
        return GetParentSize();
    }

    java.util.List<GuiElement> children;

    public void Render(Vector3f offset) {

    }
}
