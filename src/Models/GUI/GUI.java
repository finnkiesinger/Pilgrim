package Models.GUI;

import java.util.List;
import java.util.ArrayList;

public class GUI {
    private final List<GuiElement> elements;

    public GUI() {
        this.elements = new ArrayList<>();
    }

    public void Render() {
        for (GuiElement element : elements) {
            element.Render();
        }
    }

    public void AddElement(GuiElement element) {
        elements.add(element);
    }
}
