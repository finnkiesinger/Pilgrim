package Models.GUI;

import org.joml.Vector3f;

import java.util.List;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class GUI {
    private final List<GuiElement> elements;

    public GUI() {
        this.elements = new ArrayList<>();
    }

    public void Render() {
        glDisable(GL_DEPTH_TEST);
        for (GuiElement element : elements) {
            element.Render(new Vector3f());
        }
        glEnable(GL_DEPTH_TEST);
    }

    public void AddElement(GuiElement element) {
        elements.add(element);
    }
}
