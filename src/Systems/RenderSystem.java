package Systems;

import Components.DirectionalLightComponent;
import Components.ModelComponent;
import Components.PointLightComponent;
import Components.TransformComponent;
import ECS.Entity;
import ECS.Registry;
import Models.Model;

import java.util.List;

public class RenderSystem extends ECS.System {
    public RenderSystem() {
        super();
        RequireComponent(TransformComponent.class);
        RequireComponent(ModelComponent.class);
    }

    public void Update(Registry registry) {
        List<Entity> directionalLights =
                registry.GetSystem(DirectionalLightSystem.class)
                        .GetSystemEntities();

        List<Entity> pointLights =
                registry.GetSystem(PointLightSystem.class)
                        .GetSystemEntities();

        for (Entity entity : GetSystemEntities()) {
             Model model = entity.GetComponent(ModelComponent.class).model;

             model.Draw("model", entity, directionalLights, pointLights);
        }
    }
}
