package Models;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private static Scene scene;

    private List<Light> lights;
    private Camera activeCamera;

    public Scene() {
        this.lights = new ArrayList<>();

    }

    public void Load() {

    }
}
