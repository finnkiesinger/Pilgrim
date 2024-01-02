package Utilities;

import java.net.URL;

import java.util.List;

public class ResourceLoader {
    public static String GetPath(String path) {
        URL url = ResourceLoader.class.getClassLoader().getResource(path);
        if (url != null) {
            return url.getPath();
        }
        return "NOT FOUND";
    }

    public static String GetDirectory(String path) {
        String[] comps = path.split("/");
        return String.join("/", List.of(comps).subList(0, comps.length-1));
    }
}
