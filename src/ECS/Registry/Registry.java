package ECS.Registry;

import ECS.Entity.Entity;

import java.util.List;

public class Registry {
    private int numEntities;
    private List<Long> freeIds;

    public Entity CreateEntity() {
        long ID;

        if (!freeIds.isEmpty()) {
            ID = freeIds.get(0);
            freeIds.remove(ID);
        } else {
            ID = numEntities;
        }

        return new Entity(this, ID);
    }

    public void RemoveEntity(Entity entity) {

    }
}
