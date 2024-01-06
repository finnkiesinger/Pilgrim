package Systems;

import Components.PointLightComponent;
import Components.TransformComponent;
import ECS.System;

public class PointLightSystem extends System {
    public PointLightSystem() {
        super();
        RequireComponent(PointLightComponent.class);
        RequireComponent(TransformComponent.class);
    }
}
