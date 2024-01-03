package Utilities;

public enum Platform {
    UNIX,
    WINDOWS;

    public static Platform GetPlatform() {
        String os = System.getProperty("os.name");

        if (os.toLowerCase().contains("windows")) {
            return Platform.WINDOWS;
        }

        return Platform.UNIX;
    }
}
