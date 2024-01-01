package Models;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private final String path;
    private int ID;

    public Texture(int width, int height, ByteBuffer data) {
        this.path = "";
        GenerateTexture(width, height, data);
    }

    public Texture(String path) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            this.path = path;
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer data = stbi_load(path, w, h, channels, 4);
            if (data == null) {
                throw new RuntimeException("Image file {" + path + "} not loaded: " + stbi_failure_reason());
            }

            int width = w.get();
            int height = h.get();

            GenerateTexture(width, height, data);

            stbi_image_free(data);
        }
    }

    private void GenerateTexture(int width, int height, ByteBuffer data) {
        ID = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, ID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        glGenerateMipmap(GL_TEXTURE_2D);
    }

    public void Cleanup() {
        glDeleteTextures(ID);
    }

    public void Bind() {
        glBindTexture(GL_TEXTURE_2D, ID);
    }

    public String GetPath() {
        return this.path;
    }
}
