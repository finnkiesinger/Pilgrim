package Components;

import ECS.Component;
import org.joml.Vector3f;

public class PositionComponent extends Component {
    Vector3f position;

    public PositionComponent() {
        this.position = new Vector3f();
    }
    public PositionComponent(Vector3f position) {
        this.position = position;
    }
    public PositionComponent(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
    }
}
