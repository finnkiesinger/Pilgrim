package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Model {
    private final List<Material> materials;

    private final List<Mesh> transparent;
    private final List<Mesh> opaque;

    public Model(List<Material> materials) {
        this.transparent = new ArrayList<>();
        this.opaque = new ArrayList<>();
        this.materials = materials;
    }

    public Model(List<Mesh> meshes, List<Material> materials) {
        this.materials = materials;
        this.transparent = new ArrayList<>();
        this.opaque = new ArrayList<>();

        for (Mesh mesh : meshes) {
            mesh.SetModel(this);
            if (mesh.GetMaterial().GetDiffuse().w != 1) {
                transparent.add(mesh);
            } else {
                opaque.add(mesh);
            }
        }
    }

    public void DrawOpaque(String shader) {
        opaque.forEach(mesh -> mesh.Draw(shader));
    }

    public void DrawTransparent(String shader) {
        transparent.stream().sorted(Comparator.naturalOrder()).forEach(mesh -> mesh.Draw(shader));
    }

    public void Draw(String shader) {
        DrawOpaque(shader);
        DrawTransparent(shader);
    }

    public Material GetMaterial(int index) {
        try {
            return materials.get(index);
        } catch (Exception e) {
            return null;
        }
    }

    public void AddMesh(Mesh mesh) {
        if (mesh.GetMaterial().GetDiffuse().w != 1) {
            transparent.add(mesh);
        } else {
            opaque.add(mesh);
        }
    }
}
