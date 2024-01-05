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

    private final int VAO;

    private final List<Integer> VBOs;

    private int materialIndex;

    private Model model;

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

        VBO = glGenBuffers();
        VBOs.add(VBO);
        buffer = MemoryUtil.memAllocFloat(normals.length);
        buffer.put(0, normals);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        VBO = glGenBuffers();
        VBOs.add(VBO);
        buffer = MemoryUtil.memAllocFloat(textures.length);
        buffer.put(0, textures);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(2);

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

    public void SetMaterialIndex(int index) {
        this.materialIndex = index;
    }

    public void SetModel(Model model) {
        this.model = model;
    }

    private void SetUniform(int location, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            value.get(buffer);
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void Draw(String shaderName) {
        try {
            ShaderLibrary.Instance().Use(shaderName);
            Shader shader = ShaderLibrary.Instance().GetActive();
            shader.Set("texture_diffuse", 0);
            shader.Set("texture_specular", 1);

            Matrix4f modelMatrix = new Matrix4f();
            Matrix4f view = Camera.GetLookAt();
            Matrix4f projection = Camera.GetProjection();

            shader.Set("model", modelMatrix);
            shader.Set("view", view);
            shader.Set("projection", projection);

            Material material = model.GetMaterial(materialIndex);
            Texture texture = material.GetTexture();
            glActiveTexture(GL_TEXTURE0);
            texture.Bind();

            glBindVertexArray(VAO);
            glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);
        } catch (Exception ignored) {}
    }
}
