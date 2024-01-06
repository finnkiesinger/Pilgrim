package Models;

import Components.DirectionalLightComponent;
import Components.PointLightComponent;
import Components.TransformComponent;

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

    public void DrawOpaque(String shader, TransformComponent transform, List<DirectionalLightComponent> directionalLights, List<PointLightComponent> pointLights) {
        opaque.forEach(mesh -> mesh.Draw(shader, transform, directionalLights, pointLights));
    }

    public void DrawTransparent(String shader, TransformComponent transform, List<DirectionalLightComponent> directionalLights, List<PointLightComponent> pointLights) {
        transparent.stream().sorted(Comparator.naturalOrder()).forEach(mesh -> mesh.Draw(shader, transform, directionalLights, pointLights));
    }

    public void Draw(String shader, TransformComponent transform, List<DirectionalLightComponent> directionalLights, List<PointLightComponent> pointLights) {
        DrawOpaque(shader, transform, directionalLights, pointLights);
        DrawTransparent(shader, transform, directionalLights, pointLights);
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
