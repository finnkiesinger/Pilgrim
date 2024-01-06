package Models;

import org.joml.Vector3f;

public class BoundingBox {
    protected Vector3f a;
    protected Vector3f b;

    public BoundingBox(Vector3f a, Vector3f b) {
        this.a = a;
        this.b = b;
    }

    public Vector3f GetCenter() {
        return a.add(b, new Vector3f()).div(2);
    }
}
