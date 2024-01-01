package Exceptions;

public class ShaderNotFoundException extends Exception {
    public ShaderNotFoundException(String shaderName) {
        super("Shader {" + shaderName + "} was not found!");
    }
}
