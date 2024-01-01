package Systems;

import Components.PositionComponent;
import Components.VelocityComponent;

public class MovementSystem extends ECS.System {
    public MovementSystem() {
        super();

        RequireComponent(PositionComponent.class);
        RequireComponent(VelocityComponent.class);
    }

    public void Update(float deltaTime) {

    }
}
