package Game;
import ECS.Registry;
import EventBus.EventBus;
import Models.Mesh;
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

        float[] vertices = {
                0.5f,  0.5f, 0.0f,  // top right
                0.5f, -0.5f, 0.0f,  // bottom right
                -0.5f, -0.5f, 0.0f,  // bottom left
                -0.5f,  0.5f, 0.0f
        };

        int[] indices = {  // note that we start from 0!
                0, 1, 3,   // first triangle
                1, 2, 3    // second triangle
        };

        Mesh mesh = new Mesh(vertices, vertices, null, indices);
        this.model = new Model(new Mesh[]{mesh});
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
