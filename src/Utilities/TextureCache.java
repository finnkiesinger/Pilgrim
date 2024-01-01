package Utilities;

import Models.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureCache {
    Map<String, Texture> textures;

    private TextureCache() {
        this.textures = new HashMap<>();
    }

    public Texture GetTexture(String path) {
        try {
        } catch(Exception e) {

        }
    }

    private Texture LoadTexture(String path) {
        return new Texture(path);
    }

    public void Cleanup() {
        textures.values().forEach(Texture::Cleanup);
    }
}
