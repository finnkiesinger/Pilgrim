package Window;

import Game.Game;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private static final Set<Integer> pressedKeys = new HashSet<>();
    private static final Set<Integer> releasedKeys = new HashSet<>();
    private static final Set<Integer> heldKeys = new HashSet<>();

    public static final GLFWKeyCallbackI handler = (window, key, scancode, action, mods) -> {
        if (action == GLFW_PRESS) {
            pressedKeys.add(key);
            heldKeys.add(key);
        }
        if (action == GLFW_RELEASE) {
            heldKeys.remove(key);
            releasedKeys.add(key);
        }
    };
    public static void Clear() {
        pressedKeys.clear();
        releasedKeys.clear();
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
