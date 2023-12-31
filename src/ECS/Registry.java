package ECS;

import java.util.*;

public class Registry {
    private int numEntities;
    private List<Long> freeIds;

    private final List<Entity> entitiesToAdd;
    private final List<Entity> entitiesToRemove;

    private final Map<Long, BitSet> entityComponentSignatures;
    private final Map<Integer, Pool<? extends Component>> componentPools;
    private final Map<Class<? extends System>, System> systems;

    private final Map<String, HashMap<Long, Long>> entitiesPerGroup;
    private final Map<Long, HashMap<String, String>> groupsPerEntity;
    private final Map<Long, HashMap<String, String>> tagsPerEntity;

    public Registry() {
        this.entitiesToAdd = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
        this.entityComponentSignatures = new HashMap<>();
        this.systems = new HashMap<>();
        this.componentPools = new HashMap<>();
        this.entitiesPerGroup = new HashMap<>();
        this.groupsPerEntity = new HashMap<>();
        this.tagsPerEntity = new HashMap<>();

    }

    public Entity CreateEntity() {
        long ID;

        if (!freeIds.isEmpty()) {
            ID = freeIds.getFirst();
            freeIds.removeFirst();
        } else {
            ID = numEntities++;
        }

        Entity newEntity = new Entity(this, ID);
        entitiesToAdd.add(newEntity);
        return newEntity;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> void AddComponent(Entity entity, T component) {
        int id = ComponentManager.GetID(component.getClass());
        entityComponentSignatures.get(entity.GetID()).set(id);

        componentPools.putIfAbsent(id, new Pool<T>());

        ((Pool<T>) componentPools.get(id)).Set(entity.GetID(), component);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> boolean HasComponent(Entity entity, Class<T> componentClass) {
        Pool<T> pool = (Pool<T>) componentPools.get(ComponentManager.GetID(componentClass));

        if (pool == null) {
            return false;
        }

        T component = pool.Get(entity.GetID());

        return component != null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> void RemoveComponent(Entity entity, Class<T> componentClass) {
        Pool<T> pool = (Pool<T>) componentPools.get(ComponentManager.GetID(componentClass));

        if (pool == null) {
            return;
        }

        pool.RemoveEntityFromPool(entity.GetID());
    }

    public void RemoveEntity(Entity entity) {
        entitiesToRemove.add(entity);
    }

    public void Update() {
        for (Entity e: entitiesToAdd) {
            AddEntityToSystems(e);
        }
        entitiesToAdd.clear();

        for (Entity e: entitiesToRemove) {
            RemoveEntityFromSystems(e);
            entityComponentSignatures.get(e.GetID()).clear();

            for (Pool<? extends Component> pool: componentPools.values()) {
                pool.RemoveEntityFromPool(e.GetID());
            }

            freeIds.add(e.GetID());
        }
    }

    public <T extends System> boolean HasSystem(Class<T> systemClass) {
        return systems.get(systemClass) != null;
    }

    public <T extends System> T GetSystem(Class<T> systemClass) {
        return systemClass.cast(systems.get(systemClass));
    }

    public <T extends System> void AddSystem(T system) {
        systems.putIfAbsent(system.getClass(), system);
    }

    private void AddEntityToSystems(Entity entity) {

    }

    private void RemoveEntityFromSystems(Entity entity) {

    }

    public void AddEntityToGroup(Entity entity, String group) {
        long entityID = entity.GetID();

        HashMap<Long, Long> entities = entitiesPerGroup.getOrDefault(group, new HashMap<>());
        entities.put(entityID, entityID);
        entitiesPerGroup.put(group, entities);

        HashMap<String, String> groups = groupsPerEntity.getOrDefault(entityID, new HashMap<>());
        groups.put(group, group);
        groupsPerEntity.put(entityID, groups);
    }

    public void RemoveEntityFromGroup(Entity entity, String group) {
        long entityID = entity.GetID();


    }
}
