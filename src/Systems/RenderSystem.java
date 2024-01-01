package Systems;

import Components.ModelComponent;
import Components.PositionComponent;
import ECS.System;

public class RenderSystem extends System {
    public RenderSystem() {
        super();
        RequireComponent(PositionComponent.class);
        RequireComponent(ModelComponent.class);
    }

    public void Update() {

    }
}
