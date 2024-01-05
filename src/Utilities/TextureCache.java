package Utilities;

import Models.Texture;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TextureCache {
    private static final String DEFAULT = ResourceLoader.GetPath("textures/default.png");
    private static final TextureCache instance = new TextureCache();

    public static TextureCache GetInstance() {
        return instance;
    }

    private final Map<String, Texture> textures;

    private TextureCache() {
        this.textures = new HashMap<>();
        textures.put(DEFAULT, LoadTexture(DEFAULT));
    }

    public Texture GetTexture(String path) {
        Texture texture = null;
        if (path != null) {
            texture = textures.getOrDefault(path, LoadTexture(path));
            textures.putIfAbsent(path, texture);
        }
        if (texture == null) {
            texture = textures.get(DEFAULT);
        }
        return texture;
    }

    public int Size() {
        return textures.size();
    }

    public Set<String> GetTextureNames() {
        return textures.keySet();
    }

    private Texture LoadTexture(String path) {
        return new Texture(path);
    }

    public void Cleanup() {
        textures.values().forEach(Texture::Cleanup);
    }
}
