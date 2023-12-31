package ECS.Entity;

import ECS.Registry.Registry;

import java.util.HashSet;
import java.util.Set;

public class Entity {
    private long ID;
    private String tag;
    private final Set<String> groups;
    private Registry registry;

    public long GetID() {
        return this.ID;
    }

    void SetTag(String tag) {
        this.tag = tag;
    }

    String Tag() {
        return this.tag;
    }

    boolean HasTag(String tag) {
        return this.tag.equals(tag);
    }

    void AddGroup(String group) {
        this.groups.add(group);
    }

    void RemoveGroup(String group) {
        this.groups.removeIf(f -> f.equals(group));
    }

    boolean BelongsToGroup(String group) {
        return this.groups.contains(group);
    }

    public Entity(Registry registry, long ID) {
        this.groups = new HashSet<>();
        this.ID = ID;
    }
}
