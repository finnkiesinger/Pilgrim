package ECS;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class System {
    private final BitSet signature;
    private final List<Entity> entities;

    public System() {
        this.entities = new ArrayList<>();
        this.signature = new BitSet();
    }

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
        signature.set(ComponentManager.GetID(componentClass));
    }

    public boolean SignatureMatches(BitSet signature) {
        BitSet copy = (BitSet) signature.clone();
        copy.and(this.signature);

        return copy.equals(this.signature);
    }
}
