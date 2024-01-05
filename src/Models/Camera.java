package Models;

import Window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.joml.Math.toRadians;

public class Camera {
    private static Camera camera = new Camera();
    private final Vector3f position = new Vector3f(0.0f, 1.0f, 10.0f);
    private final Vector3f rotation = new Vector3f();

    private final Vector3f front = new Vector3f();
    private Vector3f right = new Vector3f();
    private Vector3f up = new Vector3f();

    private static final float pitchLimit = 89.0f;

    public Camera() {
        _Rotate(0, 0, 0);
    }

    private void _Rotate(float pitch, float yaw, float roll) {
        rotation.x += pitch;
        if (rotation.x > pitchLimit) {
            rotation.x = pitchLimit;
        }
        if (rotation.x < -pitchLimit) {
            rotation.x = -pitchLimit;
        }
        rotation.y += yaw;
        rotation.y %= 360.0f;
        rotation.z += roll;
        rotation.z %= 360.0f;

        front.x = (float) (Math.cos(toRadians(rotation.y - 90)) * Math.cos(toRadians(rotation.x)));
        front.y = (float) Math.sin(toRadians(rotation.x));
        front.z = (float) (Math.sin(toRadians(rotation.y - 90)) * Math.cos(toRadians(rotation.x)));
        front.normalize();

        right = front.cross(new Vector3f(0.0f, 1.0f, 0.0f) , new Vector3f()).normalize();

        up = right.cross(front, new Vector3f()).normalize();
    }

    /**
     * Default (in local space): x: right/left, y: up/down, z: forward/backward
     * @param translation Translation along each axis
     */
    private void _Translate(Vector3f translation) {
        position.add(right.mul(translation.x, new Vector3f()));
        position.add(up.mul(translation.y, new Vector3f()));
        position.add(front.mul(translation.z, new Vector3f()));
    }

    /**
     * Translation in world space
     * @param translation Translation along each axis, but in world space
     */
    private void _GlobalTranslate(Vector3f translation) {
        position.add(translation);
    }

    /**
     * Rotation
     * @param pitch in degrees
     * @param yaw in degrees
     * @param roll in degrees
     */
    public static void Rotate(float pitch, float yaw, float roll) {
        camera._Rotate(pitch, yaw, roll);
    }

    public static void Translate(Vector3f translation) {
        camera._Translate(translation);
    }

    public static void GlobalTranslate(Vector3f translation) {
        camera._GlobalTranslate(translation);
    }

    public Matrix4f _GetLookAt() {
        return new Matrix4f().lookAt(position, position.add(front, new Vector3f()), new Vector3f(0, 1, 0));
    }

    public static Matrix4f GetLookAt() {
        return camera._GetLookAt();
    }

    private Matrix4f _GetProjection() {
        return new Matrix4f().perspective(toRadians(45.0f), Window.ActiveWindow().GetAspectRatio(), 0.1f, 100.0f);
    }

    public static Matrix4f GetProjection() {
        return camera._GetProjection();
    }
}
