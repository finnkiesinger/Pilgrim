package Utilities;

import java.util.List;

public class FileSystem {
    public static String ConvertAbsolutePath(String path, Platform platform) {
        if (platform == Platform.WINDOWS) {
            String[] comps = path.split("/");
            return String.join("/", List.of(comps).subList(1, comps.length));
        }
        return path;
    }
}
