package Utilities;

import java.net.URL;

public class ResourceLoader {
    public static String loadResource() {
        URL url = ResourceLoader.class.getClassLoader().getResource("demo_car/scene.gltf");
        if (url != null) {
            return url.getPath();
        }
        return "NOT FOUND";
    }
}
