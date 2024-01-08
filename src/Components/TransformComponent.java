package Components;

import ECS.Component;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TransformComponent extends Component {
    private Vector3f position;

    public TransformComponent(Vector3f position) {
        this.position = position;
        UpdateTransform();
    }

    public TransformComponent() {
        this.position = new Vector3f();
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

    private void UpdateTransform() {
        this.transform = new Matrix4f().translate(position);
    }
}
