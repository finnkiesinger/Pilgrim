package Components;

import ECS.Component;
import org.joml.Vector3f;

public class TransformComponent extends Component {
    public Vector3f position;

    public TransformComponent(Vector3f position) {
        this.position = position;
    }

    public TransformComponent() {
        this.position = new Vector3f();
    }
}
