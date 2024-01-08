package Systems;

import Components.EnvironmentComponent;
import ECS.Entity;
import ECS.System;

public class EnvironmentRenderSystem extends System {
    public EnvironmentRenderSystem() {
        super();
        RequireComponent(EnvironmentComponent.class);
    }

    public void Update() {
        if (GetSystemEntities().isEmpty()) {
            return;
        }

        Entity environmentEntity = GetSystemEntities().getFirst();
        EnvironmentComponent environment = environmentEntity.GetComponent(EnvironmentComponent.class);

        environment.skybox.Draw();
    }
}
