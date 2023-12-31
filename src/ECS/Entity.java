package ECS;

public class Entity implements Comparable<Entity> {
    private final long ID;
    private Registry registry;

    public long GetID() {
        return this.ID;
    }

    void SetTag(String tag) {

    }

    String Tag() {
        return null;
    }

    boolean HasTag(String tag) {
        return false;
    }

    void AddGroup(String group) {

    }

    void RemoveGroup(String group) {

    }

    boolean BelongsToGroup(String group) {
        return false;
    }

    public Entity(Registry registry, long ID) {
        this.ID = ID;
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
