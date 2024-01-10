package Models.GUI;

import Models.Shader;
import Models.Size;
import Utilities.ShaderLibrary;
import Window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL40.*;

public class Container extends GuiElement {
    private static final float[] uv = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
    };

    private GuiElement child;
    private ContainerStyle style;

    private final int VAO;
    private final int VBO;

    public Container(GuiElement child) {
        this();
        this.style = new ContainerStyle(0, 0);
        this.child = child;
        this.child.SetParent(this);
    }

    private Container() {
        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, 6 * 4 * 2, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        int UV = glGenBuffers();
        FloatBuffer buffer = MemoryUtil.memAllocFloat(uv.length);
        buffer.put(0, uv);
        glBindBuffer(GL_ARRAY_BUFFER, UV);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public Container(GuiElement child, ContainerStyle style) {
        this(child);
        this.style = style;
        CalculateSize();
        child.CalculateSize();
    }

    protected void CalculateSize() {
        size = new Size(style.width, style.height);
    }

    public void Render(Vector3f offset) {
        try {
            Shader shader = ShaderLibrary.Instance().Use("container");
            shader.Set("projection", new Matrix4f().ortho(0.0f, Window.ActiveWindow().GetWidth(), 0.0f, Window.ActiveWindow().GetHeight(), 1.0f, -1.0f));
            shader.Set("background", style.background);
            shader.Set("width", (float) style.width);
            shader.Set("height", (float) style.height);
            shader.Set("radius", (float) style.borderRadius);
            glBindVertexArray(VAO);

            float x = offset.x();
            float y = offset.y();
            float height = child.size.height;
            float width = child.size.width;

            float[] vertices = {
                    x, y + height,
                    x, y,
                    x + width, y,
                    x, y + height,
                    x + width, y,
                    x + width, y + height,
            };

            FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
            buffer.put(0, vertices);

            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            MemoryUtil.memFree(buffer);

            glDrawArrays(GL_TRIANGLES, 0, 6);

            glBindVertexArray(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (child != null) {
            child.Render(offset);
        }
    }
}
