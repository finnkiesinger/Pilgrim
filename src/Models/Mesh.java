package Models;

import Utilities.ShaderLibrary;
import Window.Window;
import org.joml.Matrix4f;
import static org.joml.Math.*;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class Mesh {
    private float[] vertices;
    private float[] normals;
    private int[] indices;

    int VAO;

    List<Integer> VBOs;

    public Mesh(float[] vertices, float[] normals, float[] textures, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
        this.normals = normals;
        VBOs = new ArrayList<>();

        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        // vertices
        int VBO = glGenBuffers();
        VBOs.add(VBO);
        FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
        buffer.put(0, vertices);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        /*
        // normals
        VBO = glGenBuffers();
        VBOs.add(VBO);
        buffer = MemoryUtil.memAllocFloat(normals.length);
        buffer.put(0, normals);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);
         */

        // indices
        VBO = glGenBuffers();
        VBOs.add(VBO);
        IntBuffer eboBuffer = MemoryUtil.memAllocInt(indices.length);
        eboBuffer.put(0, indices);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, VBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, eboBuffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(eboBuffer);

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private void SetUniform(int location, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            value.get(buffer);
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void Draw(String shader) {
        try {
            ShaderLibrary.Instance().Use(shader);
            int modelUniform = glGetUniformLocation(ShaderLibrary.Instance().GetActiveID(), "model");
            int viewUniform = glGetUniformLocation(ShaderLibrary.Instance().GetActiveID(), "view");
            int projectionUniform = glGetUniformLocation(ShaderLibrary.Instance().GetActiveID(), "projection");

            Matrix4f model = new Matrix4f();
            Matrix4f view = new Matrix4f();
            Matrix4f projection = new Matrix4f().perspective(toRadians(45.0f), Window.ActiveWindow().GetAspectRatio(), 0.1f, 100.f);

            SetUniform(modelUniform, model);
            SetUniform(viewUniform, view);
            SetUniform(projectionUniform, projection);

            glBindVertexArray(VAO);
            glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);
        } catch (Exception ignored) {}
    }
}
