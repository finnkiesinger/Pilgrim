package Utilities;

import org.lwjgl.PointerBuffer;
import org.lwjgl.util.freetype.FT_Face;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.util.freetype.FreeType.*;

public class FontLibrary {
    private static FontLibrary instance;
    private static boolean initialized = false;

    public static FontLibrary Instance() {
        if (!initialized) {
            throw new RuntimeException("Font Library was never initialized");
        }
        return instance;
    }

    private final Map<String, Font> fonts;
    private final long library;

    private FontLibrary(long library) {
        this.library = library;
        fonts = new HashMap<>();
    }

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

        Font font = new Font(face);

        instance.fonts.put("Inter", font);
        initialized = true;

        FT_Done_Face(face);
    }

    public static void Dispose() {
        FT_Done_FreeType(Instance().library);
    }

    public static Font Get() {
        return Instance().fonts.get("Inter");
    }

    public static Font Get(String name) {
        return Instance().fonts.getOrDefault(name, Instance().fonts.get("Inter"));
    }
}
