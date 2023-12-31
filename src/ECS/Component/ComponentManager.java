package ECS.Component;

import java.util.HashMap;

public class ComponentManager {
    private static long nextId = 0;
    private static final HashMap<Class<?>, Long> _idMap = new HashMap<>();

    public static <T extends Component> long GetID(Class<T> componentClass) {
        long id = _idMap.getOrDefault(componentClass, -1L);
        if (id < 0) {
            id = nextId++;
            _idMap.put(componentClass, id);
        }
        return id;
    }
}
