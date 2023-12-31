package ECS;


import java.util.HashMap;
import java.util.Map;

public class Pool<T extends Component> {
    private final Map<Long, T> componentForEntity;

    public Pool() {
        this.componentForEntity = new HashMap<>();
    }

    public int Size() {
        return this.componentForEntity.size();
    }

    public boolean IsEmpty() {
        return this.componentForEntity.isEmpty();
    }

    public void RemoveEntityFromPool(long ID) {
        componentForEntity.remove(ID);
    }

    public T Get(long ID) {
        return componentForEntity.get(ID);
    }

    public void Set(long ID, T component) {
        componentForEntity.put(ID, component);
    }

    public void Clear() {
        this.componentForEntity.clear();
    }
}
