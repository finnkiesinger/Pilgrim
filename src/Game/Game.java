package Game;
import ECS.Registry;
import Window.Window;

public class Game {
    private final Window window;
    private final Registry registry;

    public Game(Window window) {
        this.registry = new Registry();
        this.window = window;
    }

    public void Run() {
        window.Run(this);
    }

    public void Update(float deltaTime) {

    }

    public void Render() {

    }
}
