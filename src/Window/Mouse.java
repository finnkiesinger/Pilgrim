package Window;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    private static float x, y, deltaX, deltaY;
    private static boolean initial;

    private static final Set<Integer> pressedKeys = new HashSet<>();
    private static final Set<Integer> heldKeys = new HashSet<>();
    private static final Set<Integer> releasedKeys = new HashSet<>();

    public static GLFWCursorPosCallbackI moveHandler = (window, xpos, ypos) -> {
        float xPos = (float) xpos;
        float yPos = (float) ypos;

        if (!initial) {
            initial = true;
            x = xPos;
            y = yPos;
            return;
        }
        deltaX = xPos - x;
        deltaY = yPos - y;

        x = xPos;
        y = yPos;
    };

    public static GLFWMouseButtonCallbackI buttonHandler = (window, button, action, mods) -> {
        if (action == GLFW_PRESS) {
            heldKeys.add(button);
            pressedKeys.add(button);
        }
        if (action == GLFW_RELEASE) {
            heldKeys.remove(button);
            releasedKeys.add(button);
        }
    };

    public static void Clear() {
        releasedKeys.clear();
        pressedKeys.clear();
        deltaX = 0;
        deltaY = 0;
    }

    public static float GetX() {
        return x;
    }

    public static float GetY() {
        return y;
    }

    public static float GetDeltaX() {
        return deltaX;
    }

    public static float GetDeltaY() {
        return deltaY;
    }

    public static boolean IsKeyPressed(int key) {
        return heldKeys.contains(key);
    }
    public static boolean IsKeyJustPressed(int key) {
        return pressedKeys.contains(key);
    }
    public static boolean IsKeyJustReleased(int key) {
        return releasedKeys.contains(key);
    }
}
