package Game;
import ECS.Registry;
import EventBus.EventBus;
import Models.*;
import Utilities.ModelLoader;
import Utilities.ShaderLibrary;
import Utilities.TextureCache;
import Window.*;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

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
        ShaderLibrary.Instance().Load("default", "default");
        ShaderLibrary.Instance().Load("model", "model");
        model = ModelLoader.Load("demo_car/scene.gltf");
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

        float distance = 5.0f * deltaTime;

        if (Input.IsKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            distance *= 2;
        }

        if (Input.IsKeyPressed(GLFW_KEY_A)) {
            Camera.Translate(new Vector3f(-distance, 0, 0 ));
        }
        if (Input.IsKeyPressed(GLFW_KEY_D)) {
            Camera.Translate(new Vector3f(distance, 0, 0));
        }
        if (Input.IsKeyPressed(GLFW_KEY_S)) {
            Camera.Translate(new Vector3f(0, 0, -distance));
        }
        if (Input.IsKeyPressed(GLFW_KEY_W)) {
            Camera.Translate(new Vector3f(0, 0, distance));
        }
        if (Input.IsKeyPressed(GLFW_KEY_Q)) {
            Camera.Translate(new Vector3f(0, -distance, 0));
        }
        if (Input.IsKeyPressed(GLFW_KEY_E)) {
            Camera.Translate(new Vector3f(0, distance, 0));
        }
        if (Input.IsKeyJustPressed(GLFW_KEY_H)) {
            window.ToggleHovered();
        }
        if (Mouse.IsKeyPressed(GLFW_MOUSE_BUTTON_1)) {
            Camera.Rotate(-Mouse.GetDeltaY() / 5, Mouse.GetDeltaX() / 5, 0.0f);
        }

        registry.Update();

        Render();

        Input.Clear();
        Mouse.Clear();
    }

    public void Render() {
        model.Draw("model");
    }

    public String GetTitle() {
        return this.title;
    }
}
