package Models;

import org.joml.Vector4f;

public class Material {
    public static final Vector4f DEFAULT_COLOR = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);

    Vector4f ambient;
    Vector4f diffuse;
    Vector4f specular;

    Texture texture;


    public Material() {
        ambient = DEFAULT_COLOR;
        diffuse = DEFAULT_COLOR;
        specular = DEFAULT_COLOR;
    }

    public Material(Vector4f ambient, Vector4f diffuse, Vector4f specular) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    public void SetTexture(Texture texture) {
        this.texture = texture;
    }
}
