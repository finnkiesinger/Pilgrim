package Game;
import ECS.Registry;
import EventBus.EventBus;
import Models.Model;
import Utilities.ModelLoader;
import Utilities.ShaderLibrary;
import Window.Window;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Game {
    private final Window window;
    private final Registry registry;
    private final EventBus eventBus;
    private final String title;
    private float lastFrame;

    private Model model;

    public Game(String title) {
        this.title = title;
        this.registry = new Registry();
        this.eventBus = new EventBus();
        this.window = new Window(this);
    }

    private void Setup() {
        this.lastFrame = (float) glfwGetTime();
        ShaderLibrary.getInstance().Load("default", "default");
        this.model = ModelLoader.Load("demo_car/scene.gltf");
    }

    public void Run() {
        Setup();
        window.Run();
    }

    private float GetDeltaTime() {
        float currentTime = (float) glfwGetTime();
        float deltaTime = currentTime - lastFrame;
        lastFrame = currentTime;

        return deltaTime;
    }

    public void Update() {
        float deltaTime = GetDeltaTime();

        registry.Update();

        Render();
    }

    public void Render() {
        model.Draw("default");
    }

    public String GetTitle() {
        return this.title;
    }
}
