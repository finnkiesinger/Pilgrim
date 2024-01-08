package Systems;

import Components.SpotLightComponent;
import Components.TransformComponent;
import ECS.System;

public class SpotLightSystem extends System {
    public SpotLightSystem() {
        super();
        RequireComponent(SpotLightComponent.class);
        RequireComponent(TransformComponent.class);
    }
}
