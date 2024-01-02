import Components.PositionComponent;
import Components.VelocityComponent;
import ECS.Entity;
import ECS.Registry;
import Models.Mesh;
import Systems.MovementSystem;
import Utilities.ModelLoader;
import Utilities.ResourceLoader;

public class Main {
    public static void main(String[] args) {
        //Window window = new Window();
        //Game game = new Game(window);
        //game.Run();
        System.out.println(TestCorrectEntityCreation());
        System.out.println(TestCorrectComponentBehaviour());
        String path = ResourceLoader.GetPath("demo_car/scene.gltf");
        System.out.println(path);
        System.out.println(ResourceLoader.GetDirectory(path));
        Mesh[] meshes = ModelLoader.Load("demo_car/scene.gltf");
        if (meshes != null) {
            System.out.println(meshes.length);
        }
    }

    private static boolean TestCorrectEntityCreation() {
        Registry registry = new Registry();
        Entity entity1 = registry.CreateEntity();
        Entity entity2 = registry.CreateEntity();
        registry.Update();

        entity1.Remove();
        registry.Update();
        Entity entity3 = registry.CreateEntity();
        registry.Update();
        return entity1.GetID() == 0 && entity2.GetID() == 1 && entity3.GetID() == 0;
    }

    private static boolean TestCorrectComponentBehaviour() {
        Registry registry = new Registry();
        registry.AddSystem(new MovementSystem());

        Entity entity = registry.CreateEntity();
        entity.AddComponent(new PositionComponent());
        entity.AddComponent(new VelocityComponent());

        registry.Update();

        MovementSystem system = registry.GetSystem(MovementSystem.class);
        System.out.println(system.GetSystemEntities().size());
        system.Update(0.0f);
        return true;
    }
}
