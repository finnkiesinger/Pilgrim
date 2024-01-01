package Utilities;

import Models.Material;
import Models.Model;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIScene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

public class ModelLoader {
    static Model Load(String path) {
        String url = ResourceLoader.LoadResource("demo_car/scene.gltf");
        if (url == null) {
            return null;
        }

        AIScene scene = aiImportFile(path, aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_JoinIdenticalVertices | aiProcess_FixInfacingNormals);
        if (scene == null) {
            return null;
        }

        int numMaterials = scene.mNumMaterials();
        PointerBuffer aiMaterials = scene.mMaterials();
        if (aiMaterials == null) {
            return null;
        }
        List<Material> materials = new ArrayList<>();
        for (int i = 0; i < numMaterials; i++) {
            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
        }

        return null;
    }

    private static void ProcessMaterial(AIMaterial aiMaterial, List<Material> materials) {

    }
}
