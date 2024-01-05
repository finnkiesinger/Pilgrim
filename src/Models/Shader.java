package Models;

import Utilities.ResourceLoader;
import Utilities.ShaderLibrary;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Shader {
    private int ID;

    public Shader(String vertexPath, String fragmentPath, String geometryPath) {
        String vertexShaderSource = ResourceLoader.ReadFile(vertexPath);
        String fragmentShaderSource = ResourceLoader.ReadFile(fragmentPath);
        String geometryShaderSource = null;

        if (geometryPath != null) {
            geometryShaderSource = ResourceLoader.ReadFile(geometryPath);
        }

        if (vertexShaderSource == null || fragmentShaderSource == null) {
            System.out.println("FAILED::LOAD_SHADER_SOURCE");
            return;
        }

        ID = glCreateProgram();

        if (ID == 0) {
            System.out.println("FAILED::CREATE_SHADER_PROGRAM");
            return;
        }

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);
        glAttachShader(ID, vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        glAttachShader(ID, fragmentShader);

        int geometryShader = -1;
        if (geometryShaderSource != null) {
            geometryShader = glCreateShader(GL_GEOMETRY_SHADER);
            glShaderSource(geometryShader, geometryShaderSource);
            glCompileShader(geometryShader);
            glAttachShader(ID, geometryShader);
        }

        glLinkProgram(ID);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        if (geometryShaderSource != null && geometryShader > 0) {
            glDeleteShader(geometryShader);
        }
    }

    public void Use() {
        glUseProgram(ID);
    }

    public void Cleanup() {
        glDeleteProgram(ID);
    }

    public int GetID() {
        return this.ID;
    }

    private int GetLocation(String name) {
        return glGetUniformLocation(ID, name);
    }

    public void Set(String name, Matrix4f value) {
        int location = GetLocation(name);
        FloatBuffer buffer = MemoryUtil.memAllocFloat(16);
        value.get(buffer);
        glUniformMatrix4fv(location, false, buffer);
        MemoryUtil.memFree(buffer);
    }

    public void Set(String name, Vector4f value) {
        int location = GetLocation(name);
        FloatBuffer buffer = MemoryUtil.memAllocFloat(4);
        value.get(buffer);
        glUniform4fv(location, buffer);
        MemoryUtil.memFree(buffer);
    }

    public void Set(String name, int value) {
        int location = GetLocation(name);
        glUniform1i(location, value);
    }
}
