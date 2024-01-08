package Components;

import ECS.Component;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.joml.Math.toRadians;

public class TransformComponent extends Component {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public TransformComponent() {
        this.position = new Vector3f();
        this.rotation = new Vector3f();
        this.scale = new Vector3f(1.0f);
        UpdateTransform();
    }

    public TransformComponent(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        UpdateTransform();
    }

    public Matrix4f transform;

    public void SetPosition(Vector3f position) {
        this.position = position;
        UpdateTransform();
    }

    public Vector3f GetPosition() {
        return this.position;
    }

    public void SetRotation(Vector3f rotation) {
        this.rotation = rotation;
        UpdateTransform();
    }

    public Vector3f GetRotation() {
        return this.rotation;
    }

    public void SetScale(Vector3f scale) {
        this.scale = scale;
        UpdateTransform();
    }

    public Vector3f GetScale() {
        return this.scale;
    }

    private void UpdateTransform() {
        this.transform = new Matrix4f().scale(scale).rotateXYZ(new Vector3f(toRadians(rotation.x()), toRadians(rotation.y()), toRadians(rotation.z()))).translateLocal(position);
    }
}
