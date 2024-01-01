package Utilities;

import java.net.URL;

public class ResourceLoader {
    public static String LoadResource(String path) {
        URL url = ResourceLoader.class.getClassLoader().getResource(path);
        if (url != null) {
            return url.getPath();
        }
        return "NOT FOUND";
    }
}
