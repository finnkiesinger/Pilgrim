package Models;

import org.joml.Vector3f;

public class AABB extends BoundingBox {
    public AABB(Vector3f a, Vector3f b) {
        super(a, b);
    }

    public AABB() {
        super(
                new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE),
                new Vector3f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE)
        );
    }

    public void Update(Vector3f value) {
        a.min(value);
        b.max(value);
    }
}
