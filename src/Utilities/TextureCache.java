package Utilities;

import Models.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureCache {
    private static final TextureCache instance = new TextureCache();

    public static TextureCache GetInstance() {
        return instance;
    }

    Map<String, Texture> textures;

    private TextureCache() {
        this.textures = new HashMap<>();
    }

    public Texture GetTexture(String path) {
        try {
            Texture texture = textures.getOrDefault(path, new Texture(path));
            textures.putIfAbsent(path, texture);
            return texture;
        } catch(Exception e) {
            return null;
        }
    }

    public int Size() {
        return textures.size();
    }

    private Texture LoadTexture(String path) {
        return new Texture(path);
    }

    public void Cleanup() {
        textures.values().forEach(Texture::Cleanup);
    }
}
