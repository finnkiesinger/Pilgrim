package Models;

import java.util.List;

public class Model {
    private final List<Mesh> meshes;
    private final List<Material> materials;

    public Model(List<Mesh> meshes, List<Material> materials) {
        this.meshes = meshes;
        this.materials = materials;
    }

    public void Draw(String shader) {
        for (Mesh mesh : meshes) {
            mesh.Draw(shader);
        }
    }

    public Material GetMaterial(int index) {
        try {
            return materials.get(index);
        } catch (Exception e) {
            return null;
        }
    }
}
