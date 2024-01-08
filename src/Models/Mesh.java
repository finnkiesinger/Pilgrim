package Models;

import Components.DirectionalLightComponent;
import Components.PointLightComponent;
import Components.TransformComponent;
import ECS.Entity;
import Utilities.ResourceLoader;
import Utilities.ShaderLibrary;
import Utilities.TextureCache;
import Window.Window;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import static org.joml.Math.*;

import org.joml.Vector3f;
import org.joml.Vector4f;
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

    public void Draw(
            String shaderName,
            Entity entity,
            List<Entity> directionalLights,
            List<Entity> pointLights,
            Skybox skybox
    ) {
        try {
            Shader shader = ShaderLibrary.Instance().Use(shaderName);
            shader.Set("texture_diffuse", 0);
            shader.Set("texture_specular", 1);

            Matrix4f modelMatrix = entity.GetComponent(TransformComponent.class).transform;
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
            shader.Set("material.emitting", material.GetEmitting());
            shader.Set("material.shininess", material.GetShininess());

            for (int i = 0; i < directionalLights.size(); i++) {
                DirectionalLightComponent directionalLight = directionalLights.get(i).GetComponent(DirectionalLightComponent.class);

                shader.Set("directionalLights[" + i + "].direction", directionalLight.direction);
                shader.Set("directionalLights[" + i + "].ambient", directionalLight.ambient);
                shader.Set("directionalLights[" + i + "].diffuse", directionalLight.diffuse);
                shader.Set("directionalLights[" + i + "].specular", directionalLight.specular);
            }

            for (int i = 0; i < pointLights.size(); i++) {
                Entity pointLight = pointLights.get(i);
                PointLightComponent pointLightComponent = pointLight.GetComponent(PointLightComponent.class);
                TransformComponent pointLightTransform = pointLight.GetComponent(TransformComponent.class);

                shader.Set("pointLights[" + i + "].position", pointLightTransform.GetPosition());
                shader.Set("pointLights[" + i + "].constant", pointLightComponent.constant);
                shader.Set("pointLights[" + i + "].linear", pointLightComponent.linear);
                shader.Set("pointLights[" + i + "].quadratic", pointLightComponent.quadratic);
                shader.Set("pointLights[" + i + "].ambient", pointLightComponent.ambient);
                shader.Set("pointLights[" + i + "].diffuse", pointLightComponent.diffuse);
                shader.Set("pointLights[" + i + "].specular", pointLightComponent.specular);
            }

            shader.Set("cameraPosition", Camera.GetPosition());
            shader.Set("nrPointLights", pointLights.size());
            shader.Set("nrDirectionalLights", directionalLights.size());

            Texture texture = material.GetTextureDiffuse();
            shader.Set("hasTexture", texture == TextureCache.GetInstance().GetTexture(ResourceLoader.GetPath("textures/default.png")) ? 0 : 1);
            glActiveTexture(GL_TEXTURE0);
            texture.Bind();
            texture = material.GetTextureSpecular();
            glActiveTexture(GL_TEXTURE1);
            texture.Bind();

            if (skybox != null) {
                shader.Set("hasSkybox", 1);
                skybox.GetCubemap().Bind();
            } else {
                shader.Set("hasSkybox", 0);
            }

            glBindVertexArray(VAO);
            glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);
        } catch (Exception ignored) {}
    }

    @Override
    public int compareTo(Mesh o) {
        float distance = this.aabb.GetCenter().distanceSquared(Camera.GetPosition());
        float otherDistance = o.aabb.GetCenter().distanceSquared(Camera.GetPosition());

        return (int) Math.signum(otherDistance - distance);
    }
}
