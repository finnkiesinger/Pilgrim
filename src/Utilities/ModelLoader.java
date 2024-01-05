package Utilities;

import Models.Material;
import Models.Mesh;
import Models.Model;
import Models.Texture;
import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

public class ModelLoader {
    public static Model Load(String path) {
        String url = ResourceLoader.GetPath(path);
        if (url == null) {
            System.out.println("Model does not exist");
            return null;
        }
        String directory = ResourceLoader.GetDirectory(url);

        int flags = aiProcess_GenSmoothNormals | aiProcess_JoinIdenticalVertices |
                aiProcess_Triangulate | aiProcess_FixInfacingNormals | aiProcess_CalcTangentSpace | aiProcess_LimitBoneWeights |
                aiProcess_PreTransformVertices;

        AIScene scene = aiImportFile(url, flags);

        if (scene == null) {
            System.out.println(aiGetErrorString());
            return null;
        }

        int numMaterials = scene.mNumMaterials();
        PointerBuffer aiMaterials = scene.mMaterials();
        if (aiMaterials == null) {
            System.out.println(aiGetErrorString());
            return null;
        }

        List<Material> materials = new ArrayList<>();
        for (int i = 0; i < numMaterials; i++) {
            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
            ProcessMaterial(aiMaterial, materials, directory);
        }

        int numMeshes = scene.mNumMeshes();
        PointerBuffer aiMeshes = scene.mMeshes();
        if (aiMeshes == null) {
            System.out.println(aiGetErrorString());
            return null;
        }

        Model model = new Model(materials);
        Material defaultMaterial = new Material();
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            Mesh mesh = ProcessMesh(aiMesh);
            int materialIndex = aiMesh.mMaterialIndex();
            if (materialIndex >= 0 && materialIndex < materials.size()) {
                mesh.SetMaterialIndex(materialIndex);
            } else {
                mesh.SetMaterialIndex(materials.size());
            }
            mesh.SetModel(model);
            model.AddMesh(mesh);
        }
        materials.add(defaultMaterial);

        return model;
    }

    private static void ProcessMaterial(AIMaterial aiMaterial, List<Material> materials, String texturesDirectory) {
        AIColor4D color = AIColor4D.create();

        AIString aiPath = AIString.calloc();
        Assimp.aiGetMaterialTexture(aiMaterial, aiTextureType_DIFFUSE, 0, aiPath, (IntBuffer) null, null, null, null, null, null);
        String path = aiPath.dataString();
        Texture textureDiffuse = TextureCache.GetInstance().GetTexture(ResourceLoader.GetPath("textures/default.png"));
        if (!path.isEmpty()) {
            textureDiffuse = TextureCache.GetInstance().GetTexture(texturesDirectory + "/" + path);
        }

        Vector4f ambient = Material.DEFAULT_COLOR;
        int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, color);
        if (result == aiReturn_SUCCESS) {
            ambient = new Vector4f(color.r(), color.g(), color.b(), color.a());
        }

        Vector4f diffuse = Material.DEFAULT_COLOR;
        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, color);
        if (result == aiReturn_SUCCESS) {
            diffuse = new Vector4f(color.r(), color.g(), color.b(), color.a());
        }

        Vector4f specular = Material.DEFAULT_COLOR;
        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, color);
        if (result == aiReturn_SUCCESS) {
            specular = new Vector4f(color.r(), color.g(), color.b(), color.a());
        }

        Material material = new Material(ambient, diffuse, specular);
        material.SetTextureDiffuse(textureDiffuse);
        materials.add(material);
    }

    private static Mesh ProcessMesh(AIMesh aiMesh) {
        List<Float> vertices = new ArrayList<>();
        List<Float> textures = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        ProcessVertices(aiMesh, vertices);
        ProcessNormals(aiMesh, normals);
        ProcessTextures(aiMesh, textures);
        ProcessIndices(aiMesh, indices);

        float[] vertexArray = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            vertexArray[i] = vertices.get(i);
        }
        float[] normalArray = new float[normals.size()];
        for (int i = 0; i < normals.size(); i++) {
            normalArray[i] = normals.get(i);
        }
        float[] texturesArray = new float[textures.size()];
        for (int i = 0; i < textures.size(); i++) {
            texturesArray[i] = textures.get(i);
        }
        int[] indicesArray = indices.stream().mapToInt(Integer::intValue).toArray();

        return new Mesh(vertexArray, normalArray, texturesArray, indicesArray);
    }

    private static void ProcessVertices(AIMesh aiMesh, List<Float> vertices) {
        AIVector3D.Buffer aiVertices = aiMesh.mVertices();
        while (aiVertices.remaining() > 0) {
            AIVector3D aiVertex = aiVertices.get();
            vertices.add(aiVertex.x());
            vertices.add(aiVertex.y());
            vertices.add(aiVertex.z());
        }
    }
    private static void ProcessNormals(AIMesh aiMesh, List<Float> normals) {
        AIVector3D.Buffer aiNormals = aiMesh.mNormals();
        if (aiNormals == null) {
            return;
        }
        while (aiNormals.remaining() > 0) {
            AIVector3D aiNormal = aiNormals.get();
            normals.add(aiNormal.x());
            normals.add(aiNormal.y());
            normals.add(aiNormal.z());
        }
    }
    private static void ProcessTextures(AIMesh aiMesh, List<Float> textures) {
        AIVector3D.Buffer aiTextures = aiMesh.mTextureCoords(0);
        if (aiTextures == null) {
            return;
        }

        while (aiTextures.remaining() > 0) {
            AIVector3D coords = aiTextures.get();
            textures.add(coords.x());
            textures.add(coords.y());
        }
    }
    private static void ProcessIndices(AIMesh aiMesh, List<Integer> indices) {
        AIFace.Buffer aiFaces = aiMesh.mFaces();
        int numFaces = aiMesh.mNumFaces();

        for (int i = 0; i < numFaces; i++) {
            AIFace face = aiFaces.get(i);
            IntBuffer buffer = face.mIndices();
            while (buffer.remaining() > 0) {
                indices.add(buffer.get());
            }
        }
    }
}
