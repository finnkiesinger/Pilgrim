import Components.PositionComponent;
import Components.VelocityComponent;
import ECS.ComponentManager;
import ECS.Entity;
import ECS.Registry;
import Game.Game;
import Systems.MovementSystem;
import Window.Window;

public class Main {
    public static void main(String[] args) {
        //Window window = new Window();
        //Game game = new Game(window);
        //game.Run();
        System.out.println(TestCorrectEntityCreation());
        System.out.println(TestCorrectComponentBehaviour());
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
