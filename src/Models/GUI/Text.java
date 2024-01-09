package Models.GUI;

import Models.Shader;
import Utilities.Font;
import Utilities.FontLibrary;
import Utilities.ShaderLibrary;
import Window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL40.*;

public class Text extends GuiElement {
    private static final float[] uv = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
    };

    protected int VBO;

    public String text;

    public Text(String text) {
        position = new Vector3f();
        this.text = text;
        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, 6 * 4 * 2, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        int uv = glGenBuffers();
        FloatBuffer buffer = MemoryUtil.memAllocFloat(Text.uv.length);
        buffer.put(0, Text.uv);
        glBindBuffer(GL_ARRAY_BUFFER, uv);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);
        MemoryUtil.memFree(buffer);

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void Render() {
        try {
            Shader shader = ShaderLibrary.Instance().Use("text");
            shader.Set("projection", new Matrix4f().ortho(0.0f, Window.ActiveWindow().GetWidth(), 0.0f, Window.ActiveWindow().GetHeight(), 0.1f, 100.0f));

            float x = position.x();
            for (char c : text.toCharArray()) {
                Font.Character character = FontLibrary.Get().Get(c);
                float posX = x + character.bearingX;
                float posY = position.y() - (character.height - character.bearingY);

                float[] vertices = {
                        posX, posY + character.height,
                        posX, posY,
                        posX + character.width, posY,
                        posX, posY + character.height,
                        posX + character.width, posY,
                        posX + character.width, posY + character.height,
                };

                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, character.ID);

                FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
                buffer.put(0, vertices);
                glBindBuffer(GL_ARRAY_BUFFER, VBO);
                glBufferSubData(GL_ARRAY_BUFFER, VBO, buffer);
                MemoryUtil.memFree(buffer);

                x += character.advance >> 6;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
