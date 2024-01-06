package Systems;

import Components.TransformComponent;
import Components.VelocityComponent;

public class MovementSystem extends ECS.System {
    public MovementSystem() {
        super();

        RequireComponent(TransformComponent.class);
        RequireComponent(VelocityComponent.class);
    }

    public void Update(float deltaTime) {

    }
}
