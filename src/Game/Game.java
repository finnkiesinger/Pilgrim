package Game;
import Components.*;
import ECS.Entity;
import ECS.Registry;
import EventBus.EventBus;
import Models.*;
import Models.GUI.*;
import Systems.*;
import Utilities.*;
import Window.*;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.xml.crypto.dsig.Transform;

import static org.lwjgl.glfw.GLFW.*;

public class Game {
    private final Window window;
    private final Registry registry;
    private final EventBus eventBus;
    private final String title;
    private float lastFrame;

    private GUI gui;

    public Game(String title) {
        this.title = title;
        this.registry = new Registry();
        this.eventBus = new EventBus();
        this.window = new Window(this);
        this.gui = new GUI();
    }

    private void Setup() {
        this.lastFrame = (float) glfwGetTime();
        FontLibrary.Init();
        ShaderLibrary.Instance().Load("default", "default");
        ShaderLibrary.Instance().Load("model", "model");
        ShaderLibrary.Instance().Load("skybox", "skybox");
        ShaderLibrary.Instance().Load("text", "text");
        ShaderLibrary.Instance().Load("container", "container");
        Model car = ModelLoader.Load("demo_car/scene.gltf");
        Model cuboid = ModelLoader.Load("backpack/backpack.obj");

        registry.AddSystem(new RenderSystem());
        registry.AddSystem(new CameraSystem());
        registry.AddSystem(new DirectionalLightSystem());
        registry.AddSystem(new PointLightSystem());
        registry.AddSystem(new SpotLightSystem());
        registry.AddSystem(new EnvironmentRenderSystem());

        Entity carEntity = registry.CreateEntity();
        carEntity.AddComponent(new ModelComponent(car));
        carEntity.AddComponent(new TransformComponent());

        Entity backpackEntity = registry.CreateEntity();
        backpackEntity.AddComponent(new ModelComponent(cuboid));
        backpackEntity.AddComponent(new TransformComponent(new Vector3f(2.0f, 0.0f, -10.0f), new Vector3f(0, 0, 0.0f), new Vector3f(0.5f)));

        Entity directionalLightEntity = registry.CreateEntity();
        DirectionalLightComponent directionalLight = new DirectionalLightComponent();
        directionalLight.direction = new Vector3f(1.0f, -1.0f, 1.0f);
        directionalLight.ambient = new Vector3f(0.2f, 0.2f, 0.2f);
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

        Entity spotLightEntity = registry.CreateEntity();

        SpotLightComponent spotLightComponent = new SpotLightComponent();
        spotLightComponent.cutOff = 5.0f;
        spotLightComponent.outerCutOff = 10.0f;
        spotLightComponent.ambient = new Vector3f(0.5f, 0.5f, 0.5f);
        spotLightComponent.diffuse = new Vector3f(0.8f, 0.8f, 0.8f);
        spotLightComponent.specular = new Vector3f(1.0f, 1.0f, 1.0f);
        spotLightComponent.constant = 0.5f;
        spotLightComponent.linear = 0.09f;
        spotLightComponent.quadratic = 0.032f;

        TransformComponent spotLightTransform = new TransformComponent();
        spotLightTransform.SetPosition(new Vector3f(2.0f, 0.0f, -7.0f));
        spotLightTransform.SetRotation(new Vector3f(0.0f, 180.0f, 0.0f));

        spotLightEntity.AddComponent(spotLightTransform);
        spotLightEntity.AddComponent(spotLightComponent);

        Entity environment = registry.CreateEntity();
        environment.AddComponent(new EnvironmentComponent("skybox"));

        TextStyle textStyle = new TextStyle();
        textStyle.fontSize = 32;
        textStyle.color = new Vector4f(1.0f);
        Text text = new Text("Button", textStyle);

        Center center = new Center(text);

        ContainerStyle containerStyle = new ContainerStyle();
        containerStyle.background = new Vector4f(0.0f, 0.0f, 0.0f, 0.2f);
        containerStyle.borderRadius = 16;
        Container container = new Container(center, containerStyle);

        Positioned positioned = new Positioned(container);
        positioned.x = 100.0f;
        positioned.y = 300.0f;
        positioned.width = 300;
        positioned.height = 200;

        gui.AddElement(positioned);

        registry.Update();

        Camera.SetCamera(new Camera(new Vector3f(0.0f, 1.0f, 10.0f)));
    }

    public void Run() {
        Setup();
        window.Run();
        Dispose();
    }

    public void Dispose() {
        FontLibrary.Dispose();
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

        gui.Render();

        Input.Clear();
        Mouse.Clear();
    }

    public void OnResize() {

    }

    public String GetTitle() {
        return this.title;
    }
}
