package Systems;

import Components.CameraComponent;
import ECS.System;
import Models.Camera;
import Window.Input;
import Window.Mouse;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public class CameraSystem extends System {
    public CameraSystem() {
        super();
        RequireComponent(CameraComponent.class);
    }

    public void Update(float deltaTime) {
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
        if (Mouse.IsKeyPressed(GLFW_MOUSE_BUTTON_1)) {
            Camera.Rotate(-Mouse.GetDeltaY() / 5, Mouse.GetDeltaX() / 5, 0.0f);
        }
    }
}
