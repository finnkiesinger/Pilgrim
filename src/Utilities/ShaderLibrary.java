package Utilities;

import Models.Shader;
import Exceptions.ShaderNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class ShaderLibrary {
    private static final ShaderLibrary instance = new ShaderLibrary();
    Map<String, Shader> shaders;

    private ShaderLibrary() {
        this.shaders = new HashMap<>();
    }
    public static ShaderLibrary getInstance() {
        return instance;
    }

    public void Load(String shaderName, String vertexShaderPath, String fragmentShaderPath, String geometryShaderPath) {
        Shader shader = new Shader(vertexShaderPath, fragmentShaderPath, geometryShaderPath);

        shaders.put(shaderName, shader);
    }

    public void Load(String shaderName, String shaderRoot) {
        String vertexPath = ResourceLoader.GetPath("shaders/" + shaderRoot + ".vert");
        String fragmentPath = ResourceLoader.GetPath("shaders/" + shaderRoot + ".frag");

        Shader shader = new Shader(vertexPath, fragmentPath, null);

        shaders.put(shaderName, shader);
    }

    public void Use(String shaderName) throws ShaderNotFoundException {
        Shader shader = shaders.get(shaderName);
        if (shader != null) {
            shader.Use();
            return;
        }

        throw new ShaderNotFoundException(shaderName);
    }
}
