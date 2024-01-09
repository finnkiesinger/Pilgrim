package Components;

import ECS.Component;
import Models.GUI.GuiElement;

import java.util.ArrayList;
import java.util.List;

public class GuiComponent extends Component {
    public List<GuiElement> elements;

    public GuiComponent() {
        this.elements = new ArrayList<>();
    }
}
