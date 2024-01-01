package ECS;

public class Entity implements Comparable<Entity> {
    private final long ID;
    private final Registry registry;

    public Entity(Registry registry, long ID) {
        this.registry = registry;
        this.ID = ID;
    }


    public long GetID() {
        return this.ID;
    }

    void SetTag(String tag) {
        this.registry.AddTagToEntity(this, tag);
    }

    void RemoveTag(String tag) {
        this.registry.RemoveTagFromEntity(this, tag);
    }

    boolean HasTag(String tag) {
        return this.registry.HasTag(this, tag);
    }

    void AddGroup(String group) {
        this.registry.AddEntityToGroup(this, group);
    }

    void RemoveGroup(String group) {
        this.registry.RemoveEntityFromGroup(this, group);
    }

    boolean BelongsToGroup(String group) {
        return this.registry.BelongsToGroup(this, group);
    }

    public <T extends Component> boolean HasComponent(Class<T> componentClass) {
        return this.registry.HasComponent(this, componentClass);
    }
    public <T extends Component> void AddComponent(T component) {
        this.registry.AddComponent(this, component);
    }
    public <T extends Component> void RemoveComponent(Class<T> componentClass) {
        this.registry.RemoveComponent(this, componentClass);
    }
    public <T extends Component> T GetComponent(Class<T> componentClass) {
        return this.registry.GetComponent(this, componentClass);
    }

    public void Remove() {
        this.registry.RemoveEntity(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Entity other)) {
            return false;
        }

        return other.ID == this.ID;
    }

    @Override
    public int compareTo(Entity o) {
        return (int) (this.ID - o.ID);
    }
}
