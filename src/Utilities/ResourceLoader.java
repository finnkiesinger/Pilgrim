package Utilities;

import java.io.BufferedReader;
import java.io.FileReader;
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

    public static String ReadFile(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder content = new StringBuilder();

            for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                content.append(s).append("\n");
            }

            reader.close();

            return content.toString();
        } catch(Exception e) {
            return null;
        }
    }
}
