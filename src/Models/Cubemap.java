package Models;


import Utilities.ResourceLoader;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL40.*;
import static org.lwjgl.stb.STBImage.*;

/**
 * All cubemaps are stored in the same folder, and they are named: front.jpg, back.jpg, top.jpg, etc.
 */
public class Cubemap {
    private static final String[] order = {
      "right", "left", "top", "bottom", "front", "back"
    };
    private final int ID;

    public Cubemap(String directory) {

        ID = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, ID);

        for (int i = 0; i < order.length; i++) {
            String path = ResourceLoader.GetPath(directory + "/" + order[i] + ".jpg");

            IntBuffer w = MemoryUtil.memAllocInt(1);
            IntBuffer h = MemoryUtil.memAllocInt(1);
            IntBuffer c = MemoryUtil.memAllocInt(1);

            ByteBuffer data = stbi_load(path, w, h, c, 3);

            if (data == null) {
                System.out.println("Failed to load cubemap: " + stbi_failure_reason());
                MemoryUtil.memFree(w);
                MemoryUtil.memFree(h);
                MemoryUtil.memFree(c);
                glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
                glDeleteTextures(ID);
                return;
            }

            int width = w.get();
            int height = h.get();

            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, data);

            stbi_image_free(data);
        }

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
    }

    public void Bind() {
        glBindTexture(GL_TEXTURE_CUBE_MAP, ID);
    }
}
