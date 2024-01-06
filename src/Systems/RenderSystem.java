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
        List<DirectionalLightComponent> directionalLights =
                registry.GetSystem(DirectionalLightSystem.class)
                        .GetSystemEntities()
                        .stream()
                        .map(entity -> entity.GetComponent(DirectionalLightComponent.class))
                        .toList();

        List<PointLightComponent> pointLights =
                registry.GetSystem(PointLightSystem.class)
                        .GetSystemEntities()
                        .stream()
                        .map(entity -> entity.GetComponent(PointLightComponent.class))
                        .toList();

        for (Entity entity : GetSystemEntities()) {
             Model model = entity.GetComponent(ModelComponent.class).model;
             TransformComponent transform = entity.GetComponent(TransformComponent.class);

             model.Draw("model", transform, directionalLights, pointLights);
        }
    }
}
