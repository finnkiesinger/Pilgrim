package ECS.Registry;

import ECS.Entity.Entity;

import java.util.Set;

public class Registry {
    private int numEntities;
    private Set<Long> freeIds;

    public Entity CreateEntity() {
        return new Entity(this);
    }
}
