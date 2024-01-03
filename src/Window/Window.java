package Window;

import Game.Game;
import Utilities.ShaderLibrary;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private static Window active;
    public static Window ActiveWindow() {
        return active;
    }

    private static final GLFWWindowSizeCallbackI resizeCallback = (window, width, height) -> {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);

            glfwGetWindowPos(window, x, y);
            Window.ActiveWindow().SetWidth(width);
            Window.ActiveWindow().SetHeight(height);
            glViewport(x.get(), y.get(), width, height);
        }
    };


    private final Game game;

    private boolean initialized = false;
    private long window;

    private float width;
    private float height;

    public Window(Game game) {
        active = this;
        this.game = game;
        if (!glfwInit()) {
            throw new IllegalStateException("GLFW could not be initialized");
        }

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode mode = glfwGetVideoMode(monitor);

        if (mode == null) {
            return;
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_CORE_PROFILE, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RED_BITS, mode.redBits());
        glfwWindowHint(GLFW_GREEN_BITS, mode.greenBits());
        glfwWindowHint(GLFW_BLUE_BITS, mode.blueBits());
        glfwWindowHint(GLFW_REFRESH_RATE, mode.refreshRate());

        window = glfwCreateWindow(mode.width(), mode.height(), game.GetTitle(), NULL, NULL);

        if (window == NULL) {
            throw new RuntimeException("Window.Window couldn't be created");
        }

        glfwSetKeyCallback(window, Input.handler);

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);

        GL.createCapabilities();

        glfwSetWindowSizeCallback(window, resizeCallback);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            glfwGetWindowSize(window, width, height);

            resizeCallback.invoke(window, width.get(), height.get());
        }

        initialized = true;
    }

    public void Run() {
        if (!initialized) {
            throw new RuntimeException("Initializing the window failed");
        }

        glfwShowWindow(window);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        Loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public void Loop() {
        while (!glfwWindowShouldClose(window)) {

            glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

            game.Update();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void SetWidth(float width) {
        this.width = width;
    }

    private void SetHeight(float height) {
        this.height = height;
    }

    public float GetAspectRatio() {
        return this.width / this.height;
    }
}
