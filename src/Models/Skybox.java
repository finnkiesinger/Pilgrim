package Models;

import Utilities.ShaderLibrary;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL40.*;

public class Skybox {
    private Cubemap cubemap;
    int VAO;

    public Skybox() {
        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        int VBO = glGenBuffers();

        FloatBuffer buffer = MemoryUtil.memAllocFloat(Cube.vertices.length);
        buffer.put(0, Cube.vertices);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        MemoryUtil.memFree(buffer);

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void LoadCubemap(String directory) {
        this.cubemap = new Cubemap(directory);
    }

    public void Draw() {
        glDepthMask(false);

        try {
            Shader shader = ShaderLibrary.Instance().Use("skybox");
            shader.Set("projection", Camera.GetProjection());
            shader.Set("view", new Matrix4f(new Matrix3f(Camera.GetLookAt())));
            cubemap.Bind();
            glBindVertexArray(VAO);
            glDrawArrays(GL_TRIANGLES, 0, 36);
            glBindVertexArray(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        glDepthMask(true);
    }

    public Cubemap GetCubemap() {
        return this.cubemap;
    }
}
