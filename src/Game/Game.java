package Game;
import ECS.Registry;
import EventBus.EventBus;
import Window.Window;

public class Game {
    private final Window window;
    private final Registry registry;
    private final EventBus eventBus;

    public Game(Window window) {
        this.registry = new Registry();
        this.eventBus = new EventBus();
        this.window = window;
    }

    private void Setup() {

    }

    public void Run() {
        Setup();
        window.Run(this);
    }

    public void Update(float deltaTime) {
        registry.Update();
    }

    public void Render() {

    }
}
