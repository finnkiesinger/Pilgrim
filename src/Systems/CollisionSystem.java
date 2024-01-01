package Systems;

import Components.PositionComponent;
import ECS.System;
import EventBus.EventBus;
import Events.CollisionEvent;

public class CollisionSystem extends System {
    public CollisionSystem() {
        super();
        RequireComponent(PositionComponent.class);
    }

    public void Update(EventBus eventBus) {
        eventBus.Emit(new CollisionEvent(null, null));
    }
}
