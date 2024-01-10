package Utilities;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.util.freetype.*;

import static org.lwjgl.opengl.GL40.*;
import static org.lwjgl.util.freetype.FreeType.*;

public class Font {
    public static class Character {
        public final int ID;
        public final int width;
        public final int height;
        public final int advance;
        public final int bearingX;
        public final int bearingY;

        public Character(int ID, int width, int height, int advance, int bearingX, int bearingY) {
            this.ID = ID;
            this.width = width;
            this.height = height;
            this.advance = advance;
            this.bearingX = bearingX;
            this.bearingY = bearingY;
        }
    }

    private final Map<Integer, Character> characters;

    public Font(FT_Face face) {
        this.characters = new HashMap<>();

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        for (int i = 0; i < 128; i++) {
            int result = FT_Load_Char(face, i, FT_LOAD_RENDER);

            if (result != FT_Err_Ok) {
                continue;
            }

            FT_GlyphSlot glyph = face.glyph();

            if (glyph == null) {
                continue;
            }
            FT_Bitmap bitmap = glyph.bitmap();

            int width = bitmap.width();
            int height = bitmap.rows();
            int bearingX = glyph.bitmap_left();
            int bearingY = glyph.bitmap_top();

            FT_Vector a = glyph.advance();
            int advance = (int) a.x();

            int texture = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texture);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);


            ByteBuffer data = bitmap.buffer(bitmap.width() * bitmap.rows());

            if (data != null) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, width, height, 0, GL_RED, GL_UNSIGNED_BYTE, data);
                glGenerateMipmap(GL_TEXTURE_2D);
            }

            Character character = new Character(texture, width, height, advance, bearingX, bearingY);
            characters.put(i, character);
        }
    }

    public Character Get(char c) {
        return characters.get((int) c);
    }
}
