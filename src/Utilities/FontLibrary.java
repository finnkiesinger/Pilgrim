package Utilities;

import org.lwjgl.PointerBuffer;
import org.lwjgl.util.freetype.FT_Face;

import static org.lwjgl.util.freetype.FreeType.*;

public class FontLibrary {
    private static FontLibrary instance;
    private static boolean initialized = false;

    private FontLibrary(long library) {
        this.library = library;
    }

    public static FontLibrary Instance() {
        if (!initialized) {
            throw new RuntimeException("Font Library was never initialized");
        }
        return instance;
    }

    private final long library;

    public static void Init() {
        PointerBuffer buffer = PointerBuffer.allocateDirect(1);
        int result = FT_Init_FreeType(buffer);

        if (result != FT_Err_Ok) {
            buffer.free();
            return;
        }

        instance = new FontLibrary(buffer.get(0));

        PointerBuffer faceBuffer = PointerBuffer.allocateDirect(1);
        result = FT_New_Face(instance.library, ResourceLoader.GetPath("fonts/Inter/Inter.ttf"), 0, faceBuffer);

        if (result != FT_Err_Ok) {
            faceBuffer.free();
            buffer.free();
            return;
        }

        FT_Face face = FT_Face.create(faceBuffer.get(0));

        FT_Set_Pixel_Sizes(face, 0, 48);

        initialized = true;
    }
}
