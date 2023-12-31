package ECS;

import java.util.BitSet;
import java.util.List;

public class System {
    private BitSet components;
    private List<Entity> entities;

    public void AddEntityToSystem(Entity entity) {
        entities.add(entity);
    }

    public void RemoveEntityFromSystem(Entity entity) {
        entities.remove(entity);
    }

    public List<Entity> GetSystemEntities() {
        return this.entities;
    }

    public <T extends Component> void RequireComponent(Class<T> componentClass) {
        components.set(ComponentManager.GetID(componentClass));
    }
}
