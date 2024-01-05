package Models;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Light {
    private static final Light active = new Light();

    private float intensity = 1.0f;
    private Vector4f color = new Vector4f(1.0f);
    private Vector3f position = new Vector3f();

    private Light() {
        position = new Vector3f(0.0f, 5.0f, 10.0f);
    }

    public Vector3f GetPosition() {
        return this.position;
    }

    public float GetIntensity() {
        return this.intensity;
    }

    public Vector4f GetColor() {
        return this.color;
    }

    public static Light GetActive() {
        return active;
    }
}
