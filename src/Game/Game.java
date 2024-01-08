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
        ShaderLibrary.Instance().Load("skybox", "skybox");
        Model car = ModelLoader.Load("demo_car/scene.gltf");
        Model cuboid = ModelLoader.Load("backpack/backpack.obj");

        registry.AddSystem(new RenderSystem());
        registry.AddSystem(new CameraSystem());
        registry.AddSystem(new DirectionalLightSystem());
        registry.AddSystem(new PointLightSystem());
        registry.AddSystem(new EnvironmentRenderSystem());

        Entity carEntity = registry.CreateEntity();
        carEntity.AddComponent(new ModelComponent(car));
        carEntity.AddComponent(new TransformComponent());

        Entity backpackEntity = registry.CreateEntity();
        backpackEntity.AddComponent(new ModelComponent(cuboid));
        backpackEntity.AddComponent(new TransformComponent(new Vector3f(2.0f, 0.0f, -10.0f)));

        Entity directionalLightEntity = registry.CreateEntity();
        DirectionalLightComponent directionalLight = new DirectionalLightComponent();
        directionalLight.direction = new Vector3f(1.0f, -1.0f, -1.0f);
        directionalLight.ambient = new Vector3f(0.1f, 0.1f, 0.1f);
        directionalLight.diffuse = new Vector3f(0.8f, 0.8f, 0.8f);
        directionalLight.specular = new Vector3f(1.0f, 1.0f, 1.0f);
        directionalLightEntity.AddComponent(directionalLight);

        Entity pointLightEntity = registry.CreateEntity();

        PointLightComponent pointLight = new PointLightComponent();
        pointLight.ambient = new Vector3f(0.1f, 0.1f, 0.1f);
        pointLight.diffuse = new Vector3f(0.5f, 0.5f, 0.5f);
        pointLight.specular = new Vector3f(1.0f, 1.0f, 1.0f);
        pointLight.constant = 0.5f;
        pointLight.linear = 0.09f;
        pointLight.quadratic = 0.032f;

        TransformComponent pointLightTransform = new TransformComponent();

        pointLightTransform.SetPosition(new Vector3f(0.0f, 1.0f, 5.0f));

        pointLightEntity.AddComponent(pointLight);
        pointLightEntity.AddComponent(pointLightTransform);

        Entity environment = registry.CreateEntity();
        environment.AddComponent(new EnvironmentComponent("skybox"));

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

        registry.GetSystem(CameraSystem.class).Update(deltaTime);
        registry.GetSystem(EnvironmentRenderSystem.class).Update();
        registry.GetSystem(RenderSystem.class).Update(registry);

        Input.Clear();
        Mouse.Clear();
    }

    public String GetTitle() {
        return this.title;
    }
}
