package Game;
import Components.*;
import ECS.Entity;
import ECS.Registry;
import EventBus.EventBus;
import Models.*;
import Systems.*;
import Utilities.*;
import Window.*;
import org.joml.Vector3f;

import javax.swing.*;

import static org.lwjgl.glfw.GLFW.*;

public class Game {
    private final Window window;
    private final Registry registry;
    private final EventBus eventBus;
    private final String title;
    private float lastFrame;

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
        Model model = ModelLoader.Load("demo_car/scene.gltf");

        registry.AddSystem(new RenderSystem());
        registry.AddSystem(new CameraSystem());
        registry.AddSystem(new DirectionalLightSystem());
        registry.AddSystem(new PointLightSystem());

        Entity modelEntity = registry.CreateEntity();
        modelEntity.AddComponent(new ModelComponent(model));
        modelEntity.AddComponent(new TransformComponent());

        Entity directionalLightEntity = registry.CreateEntity();
        DirectionalLightComponent directionalLight = new DirectionalLightComponent();
        directionalLight.direction = new Vector3f(1.0f, -1.0f, 1.0f);
        directionalLight.ambient = new Vector3f(0.1f, 0.1f, 0.1f);
        directionalLight.diffuse = new Vector3f(0.5f, 0.5f, 0.5f);
        directionalLight.specular = new Vector3f(1.0f, 1.0f, 1.0f);
        directionalLightEntity.AddComponent(directionalLight);

        Entity pointLightEntity = registry.CreateEntity();

        PointLightComponent pointLight = new PointLightComponent();
        pointLight.ambient = new Vector3f(0.1f, 0.1f, 0.1f);
        pointLight.diffuse = new Vector3f(0.5f, 0.5f, 0.5f);
        pointLight.specular = new Vector3f(1.0f, 1.0f, 1.0f);
        pointLight.constant = 1.0f;
        pointLight.linear = 0.09f;
        pointLight.quadratic = 0.032f;

        TransformComponent pointLightTransform = new TransformComponent();

        pointLightTransform.position = new Vector3f(0.0f, 1.0f, 5.0f);

        pointLightEntity.AddComponent(pointLight);
        pointLightEntity.AddComponent(pointLightTransform);

        registry.Update();

        Camera.SetCamera(new Camera(new Vector3f(0.0f, 1.0f, 10.0f)));
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

        if (Input.IsKeyJustPressed(GLFW_KEY_H)) {
            JFrame frame = new JFrame();
            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
            for (ECS.System system : registry.GetSystemList()) {
                JLabel systemName = new JLabel(system.getClass().getName());
                JLabel entityCount = new JLabel(String.valueOf(system.GetSystemEntities().size()));
                frame.add(systemName);
                frame.add(entityCount);
            }
            frame.pack();
            frame.setVisible(true);
        }

        registry.GetSystem(CameraSystem.class).Update(deltaTime);
        registry.GetSystem(RenderSystem.class).Update(registry);

        Input.Clear();
        Mouse.Clear();
    }

    public String GetTitle() {
        return this.title;
    }
}
