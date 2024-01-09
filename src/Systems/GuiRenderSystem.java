package Systems;

import Components.GuiComponent;
import ECS.Entity;
import ECS.System;
import Models.GUI.GuiElement;

public class GuiRenderSystem extends System {
    public GuiRenderSystem() {
        super();
        RequireComponent(GuiComponent.class);
    }

    public void Update() {
        for (Entity entity : GetSystemEntities()) {
            entity.GetComponent(GuiComponent.class).elements.forEach(GuiElement::Render);
        }
    }
}
