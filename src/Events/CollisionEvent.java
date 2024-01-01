package Events;

import ECS.Entity;
import EventBus.Event;

public class CollisionEvent extends Event {
    public final Entity a;
    public final Entity b;

    public CollisionEvent(Entity a, Entity b) {
        this.a = a;
        this.b = b;
    }
}
