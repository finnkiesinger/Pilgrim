package Utilities;

public class ShaderLibrary {
    private static final ShaderLibrary instance = new ShaderLibrary();
    private ShaderLibrary() {

    }
    public static ShaderLibrary getInstance() {
        return instance;
    }

    public void load() {

    }
}
