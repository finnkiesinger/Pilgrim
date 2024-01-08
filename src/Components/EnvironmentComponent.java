package Components;

import ECS.Component;
import Models.Cubemap;
import Models.Skybox;

public class EnvironmentComponent extends Component {
    public Skybox skybox;

    public EnvironmentComponent(String directory) {
        this.skybox = new Skybox();
        skybox.LoadCubemap(directory);
    }
}
