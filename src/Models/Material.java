package Models;

import org.joml.Vector4f;

public class Material {
    public static final Vector4f DEFAULT_COLOR = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);

    private final Vector4f ambient;
    private final Vector4f diffuse;
    private final Vector4f specular;

    private Texture textureDiffuse;


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

    public void SetTextureDiffuse(Texture textureDiffuse) {
        this.textureDiffuse = textureDiffuse;
    }

    public Texture GetTextureDiffuse() {
        return this.textureDiffuse;
    }

    public Vector4f GetAmbient() {
        return this.ambient;
    }

    public Vector4f GetDiffuse() {
        return this.diffuse;
    }

    public Vector4f GetSpecular() {
        return this.specular;
    }
}
