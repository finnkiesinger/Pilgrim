package ECS;

import java.util.HashMap;

public class ComponentManager {
    private static int nextId = 0;
    private static final HashMap<Class<?>, Integer> _idMap = new HashMap<>();

    public static <T extends Component> int GetID(Class<T> componentClass) {
        int id = _idMap.getOrDefault(componentClass, -1);
        if (id < 0) {
            id = nextId++;
            _idMap.put(componentClass, id);
        }
        return id;
    }
}
