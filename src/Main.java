import Components.PositionComponent;
import Components.VelocityComponent;
import ECS.Component.ComponentManager;
import Game.Game;
import Utilities.Signature;
import Window.Window;

public class Main {
    public static void main(String[] args) {
        Window window = new Window();
        Game game = new Game(window);
        game.Run();
        PositionComponent comp1 = new PositionComponent();
        PositionComponent comp2 = new PositionComponent();
        VelocityComponent comp3 = new VelocityComponent();


        System.out.println(ComponentManager.GetID(comp1.getClass()));
        System.out.println(ComponentManager.GetID(comp2.getClass()));
        System.out.println(ComponentManager.GetID(comp3.getClass()));
    }
}
