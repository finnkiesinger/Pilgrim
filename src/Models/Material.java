package Models;

import org.joml.Vector4f;

public class Material {
    public static final Vector4f DEFAULT_COLOR = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);

    private final Vector4f ambient;
    private final Vector4f diffuse;
    private final Vector4f specular;
    private final Vector4f emitting;
    private final float shininess;

    private Texture textureDiffuse;
    private Texture textureSpecular;


    public Material() {
        ambient = DEFAULT_COLOR;
        diffuse = DEFAULT_COLOR;
        specular = DEFAULT_COLOR;
        emitting = DEFAULT_COLOR;
        shininess = 1.0f;
    }

    public Material(Vector4f ambient, Vector4f diffuse, Vector4f specular, Vector4f emitting, float shininess) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
        this.emitting =  emitting;
    }

    public void SetTextureDiffuse(Texture textureDiffuse) {
        this.textureDiffuse = textureDiffuse;
    }
    public void SetTextureSpecular(Texture textureSpecular) {
        this.textureSpecular = textureSpecular;
    }

    public Texture GetTextureDiffuse() {
        return this.textureDiffuse;
    }
    public Texture GetTextureSpecular() {
        return this.textureSpecular;
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

    public Vector4f GetEmitting() {
        return this.emitting;
    }

    public float GetShininess() {
        return this.shininess;
    }
}
