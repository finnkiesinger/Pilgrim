package Models.GUI;

import Models.Shader;
import Models.Size;
import Utilities.Constants;
import Utilities.Font;
import Utilities.FontLibrary;
import Utilities.ShaderLibrary;
import Window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.Arrays;

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
    public TextStyle style;

    public Text(String text) {
        position = new Vector3f(0.0f, 0.0f, 0.0f);
        this.style = new TextStyle();
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

        CalculateSize();
    }

    public Text(String text, TextStyle style) {
        this(text);
        this.style = style;
        CalculateSize();
        System.out.println(size);
    }

    protected void CalculateSize() {
        int width = 0;
        int height = 0;

        for (char c : text.toCharArray()) {
            Font.Character character = FontLibrary.Get().Get(c);
            width += character.advance >> 6;
            if (height < character.height) {
                height = character.height;
            }
        }

        float scale = style.GetFontSize() / (float) Constants.FONT_SIZE;
        this.size = new Size((int) (width * scale), (int) (height * scale));
    }

    public void Render(Vector3f offset) {
        try {
            Shader shader = ShaderLibrary.Instance().Use("text");
            shader.Set("projection", new Matrix4f().ortho(0.0f, Window.ActiveWindow().GetWidth(), 0.0f, Window.ActiveWindow().GetHeight(), 1.0f, -1.0f));
            shader.Set("color", style.GetColor());
            glActiveTexture(GL_TEXTURE0);
            glBindVertexArray(VAO);

            float scale = style.GetFontSize() / (float) Constants.FONT_SIZE;

            float x = position.x() + offset.x();
            for (char c : text.toCharArray()) {
                Font.Character character = FontLibrary.Get().Get(c);
                float posX = x + character.bearingX * scale;
                float posY = position.y() + offset.y() - (character.height - character.bearingY) * scale;

                float height = character.height * scale;
                float width = character.width * scale;
                float[] vertices = {
                        posX, posY + height,
                        posX, posY,
                        posX + width, posY,
                        posX, posY + height,
                        posX + width, posY,
                        posX + width, posY + height,
                };

                glBindTexture(GL_TEXTURE_2D, character.ID);
                FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
                buffer.put(0, vertices);
                glBindBuffer(GL_ARRAY_BUFFER, VBO);
                glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
                glBindBuffer(GL_ARRAY_BUFFER, 0);
                MemoryUtil.memFree(buffer);

                glDrawArrays(GL_TRIANGLES, 0, 6);

                x += (character.advance >> 6) * scale;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
