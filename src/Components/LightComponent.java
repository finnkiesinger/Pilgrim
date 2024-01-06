package Components;

import ECS.Component;
import org.joml.Vector3f;

public class LightComponent extends Component {
    public Vector3f ambient;
    public Vector3f diffuse;
    public Vector3f specular;
}
