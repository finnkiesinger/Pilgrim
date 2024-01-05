package Window;

import Game.Game;
import Models.Camera;
import Models.Shader;
import Utilities.ResourceLoader;
import Utilities.ShaderLibrary;
import org.joml.Matrix4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.*;

import static org.joml.Math.toRadians;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private static Window active;
    public static Window ActiveWindow() {
        return active;
    }

    private static final GLFWWindowSizeCallbackI resizeCallback = (window, width, height) -> {
        Window.ActiveWindow().SetWidth(width);
        Window.ActiveWindow().SetHeight(height);

        try(MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer scaleX = stack.mallocFloat(1);
            FloatBuffer scaleY = stack.mallocFloat(1);

            glfwGetWindowContentScale(window, scaleX, scaleY);
            glViewport(0, 0, (int) (width * scaleX.get()), (int) (height * scaleY.get()));
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

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RED_BITS, mode.redBits());
        glfwWindowHint(GLFW_GREEN_BITS, mode.greenBits());
        glfwWindowHint(GLFW_BLUE_BITS, mode.blueBits());
        glfwWindowHint(GLFW_REFRESH_RATE, mode.refreshRate());

        window = glfwCreateWindow(mode.width(), mode.height(), game.GetTitle(), monitor, NULL);

        if (window == NULL) {
            PointerBuffer buffer = MemoryUtil.memAllocPointer(512);
            glfwGetError(buffer);
            System.out.println(buffer.getStringUTF8());
        }

        glfwSetKeyCallback(window, Input.handler);
        glfwSetCursorPosCallback(window, Mouse.moveHandler);
        glfwSetMouseButtonCallback(window, Mouse.buttonHandler);

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);

        GL.createCapabilities();
        GLUtil.setupDebugMessageCallback();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_MULTISAMPLE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(2);

            glfwGetWindowSize(window, width, height);
            resizeCallback.invoke(window, width.get(), height.get());
        }

        glfwSetWindowSizeCallback(window, resizeCallback);

        initialized = true;
    }

    public void Run() {
        if (!initialized) {
            throw new RuntimeException("Initializing the window failed");
        }
        glfwShowWindow(window);
        Loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    boolean hovered;

    public void ToggleHovered() {
        this.hovered = !this.hovered;
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
