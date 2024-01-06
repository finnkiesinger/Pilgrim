package Models;

import Utilities.ShaderLibrary;
import Window.Window;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import static org.joml.Math.*;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL33.*;

public class Mesh implements Comparable<Mesh> {
    private final AABB aabb;
    private float[] vertices;
    private float[] normals;
    private int[] indices;

    private final int VAO;

    private final List<Integer> VBOs;

    private int materialIndex;

    private Model model;

    public Mesh(float[] vertices, float[] normals, float[] textures, int[] indices) {
        this.aabb = new AABB();
        for (int i = 0; i < vertices.length; i += 3) {
            Vector3f vertex = new Vector3f(vertices[i], vertices[i + 1], vertices[i + 2]);
            aabb.Update(vertex);
        }
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

        // textures
        VBO = glGenBuffers();
        VBOs.add(VBO);
        buffer = MemoryUtil.memAllocFloat(textures.length);
        buffer.put(0, textures);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(2);

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

    public void SetMaterialIndex(int index) {
        this.materialIndex = index;
    }

    public void SetModel(Model model) {
        this.model = model;
    }

    public Material GetMaterial() {
        return this.model.GetMaterial(materialIndex);
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


            shader.Set("normalMatrix", modelMatrix.normal(new Matrix3f()));

            Material material = model.GetMaterial(materialIndex);
            shader.Set("material.diffuse", material.GetDiffuse());
            shader.Set("material.ambient", material.GetAmbient());
            shader.Set("material.specular", material.GetSpecular());
            shader.Set("material.shininess", material.GetShininess());

            Light light = Light.GetActive();

            shader.Set("pointLight.position", light.GetPosition());
            shader.Set("pointLight.color", light.GetColor());
            shader.Set("pointLight.intensity", light.GetIntensity());

            shader.Set("cameraPosition", Camera.GetPosition());

            Texture texture = material.GetTextureDiffuse();
            glActiveTexture(GL_TEXTURE0);
            texture.Bind();

            glBindVertexArray(VAO);
            glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);
        } catch (Exception ignored) {}
    }

    @Override
    public int compareTo(Mesh o) {
        float distance = this.aabb.GetCenter().distance(Camera.GetPosition());
        float otherDistance = o.aabb.GetCenter().distance(Camera.GetPosition());

        return (int) Math.signum(otherDistance - distance);
    }
}
