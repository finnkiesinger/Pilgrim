package Systems;

import Components.EnvironmentComponent;
import Components.ModelComponent;
import Components.TransformComponent;
import ECS.Entity;
import ECS.Registry;
import Models.Camera;
import Models.Model;
import Models.Skybox;

import java.util.Comparator;
import java.util.List;

public class RenderSystem extends ECS.System {
    public RenderSystem() {
        super();
        RequireComponent(TransformComponent.class);
        RequireComponent(ModelComponent.class);
    }

    private static final Comparator<Entity> comparator = (e1, e2) -> {
        float distance1 = e1.GetComponent(TransformComponent.class).GetPosition().distanceSquared(Camera.GetPosition());
        float distance2 = e2.GetComponent(TransformComponent.class).GetPosition().distanceSquared(Camera.GetPosition());

        return (int) Math.signum(distance2 - distance1);
    };

    public void Update(Registry registry) {
        List<Entity> directionalLights =
                registry.GetSystem(DirectionalLightSystem.class)
                        .GetSystemEntities();

        List<Entity> pointLights =
                registry.GetSystem(PointLightSystem.class)
                        .GetSystemEntities();

        List<Entity> spotLights =
                registry.GetSystem(SpotLightSystem.class)
                        .GetSystemEntities();

        Skybox skybox = null;

        EnvironmentRenderSystem environmentSystem = registry.GetSystem(EnvironmentRenderSystem.class);

        if (!environmentSystem.GetSystemEntities().isEmpty()) {
            skybox = environmentSystem.GetSystemEntities().getFirst().GetComponent(EnvironmentComponent.class).skybox;
        }

        for (Entity entity : GetSystemEntities().stream().sorted(comparator).toList()) {
             Model model = entity.GetComponent(ModelComponent.class).model;

             model.Draw("model", entity, directionalLights, pointLights, spotLights, skybox);
        }
    }
}
