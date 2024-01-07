package ECS;

import java.util.*;

public class Registry {
    private int numEntities;
    private final List<Long> freeIds;

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
        this.freeIds = new ArrayList<>();
    }

    public Entity CreateEntity() {
        long ID;

        if (!freeIds.isEmpty()) {
            ID = freeIds.getFirst();
            freeIds.removeFirst();
        } else {
            ID = numEntities++;
            entityComponentSignatures.put(ID, new BitSet());
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

    @SuppressWarnings("unchecked")
    public <T extends Component> T GetComponent(Entity entity, Class<T> componentClass) {
        Pool<T> pool = (Pool<T>) componentPools.get(ComponentManager.GetID(componentClass));

        if (pool == null) {
            return null;
        }

        return pool.Get(entity.GetID());
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

            RemoveEntityFromGroups(e);
            RemoveEntityTags(e);
        }

        entitiesToRemove.clear();
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
        BitSet signature = entityComponentSignatures.get(entity.GetID());
        for (System system: systems.values()) {
            if (system.SignatureMatches(signature)) {
                system.AddEntityToSystem(entity);
            }
        }
    }

    private void RemoveEntityFromSystems(Entity entity) {
        for (System system: systems.values()) {
            system.RemoveEntityFromSystem(entity);
        }
    }

    public void RemoveEntityFromGroups(Entity entity) {
        groupsPerEntity.remove(entity.GetID());
        entitiesPerGroup.values().forEach(entities -> {
            entities.remove(entity.GetID());
        });
    }

    public void RemoveEntityTags(Entity entity) {
        tagsPerEntity.remove(entity.GetID());
    }

    public void AddEntityToGroup(Entity entity, String group) {
        long ID = entity.GetID();

        HashMap<Long, Long> entities = entitiesPerGroup.getOrDefault(group, new HashMap<>());
        entities.put(ID, ID);
        entitiesPerGroup.putIfAbsent(group, entities);

        HashMap<String, String> groups = groupsPerEntity.getOrDefault(ID, new HashMap<>());
        groups.put(group, group);
        groupsPerEntity.putIfAbsent(ID, groups);
    }

    public void RemoveEntityFromGroup(Entity entity, String group) {
        long ID = entity.GetID();

        HashMap<Long, Long> entities = entitiesPerGroup.get(group);
        if (entities != null) {
            entities.remove(ID);
        }

        HashMap<String, String> groups = groupsPerEntity.get(ID);
        if (groups != null) {
            groups.remove(group);
        }
    }

    public void AddTagToEntity(Entity entity, String tag) {
        long ID = entity.GetID();
        HashMap<String, String> tags = tagsPerEntity.getOrDefault(ID, new HashMap<>());
        tags.put(tag, tag);
        tagsPerEntity.putIfAbsent(ID, tags);
    }

    public void RemoveTagFromEntity(Entity entity, String tag) {
        HashMap<String, String> tags = tagsPerEntity.get(entity.GetID());

        if (tags != null) {
            tags.remove(tag);
        }
    }

    public boolean BelongsToGroup(Entity entity, String group) {
        HashMap<String, String> groups = groupsPerEntity.get(entity.GetID());

        if (groups == null) {
            return false;
        }

        return groups.containsKey(group);
    }

    public boolean HasTag(Entity entity, String tag) {
        HashMap<String, String> tags = tagsPerEntity.get(entity.GetID());
        if (tags == null) {
            return false;
        }

        return tags.containsKey(tag);
    }

    public List<System> GetSystemList() {
        return this.systems.values().stream().toList();
    }
}
