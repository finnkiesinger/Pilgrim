package Models;

public class Model {
    private Mesh[] meshes;

    public Model(Mesh[] meshes) {
        this.meshes = meshes;
    }

    public void Draw(String shader) {
        for (Mesh mesh : meshes) {
            mesh.Draw(shader);
        }
    }
}
