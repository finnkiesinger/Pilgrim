import Components.PositionComponent;
import Components.VelocityComponent;
import ECS.ComponentManager;
import Game.Game;
import Window.Window;

public class Main {
    public static void main(String[] args) {
        Window window = new Window();
        Game game = new Game(window);
        game.Run();
    }
}
