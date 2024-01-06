package Systems;

import Components.DirectionalLightComponent;
import ECS.System;

public class DirectionalLightSystem extends System {
    public DirectionalLightSystem() {
        super();
        RequireComponent(DirectionalLightComponent.class);
    }
}
