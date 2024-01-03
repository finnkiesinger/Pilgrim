package Window;

import Game.Game;
import Utilities.ShaderLibrary;
import org.lwjgl.PointerBuffer;
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

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RED_BITS, mode.redBits());
        glfwWindowHint(GLFW_GREEN_BITS, mode.greenBits());
        glfwWindowHint(GLFW_BLUE_BITS, mode.blueBits());
        glfwWindowHint(GLFW_REFRESH_RATE, mode.refreshRate());

        window = glfwCreateWindow(800, 600, game.GetTitle(), NULL, NULL);

        if (window == NULL) {
            PointerBuffer buffer = MemoryUtil.memAllocPointer(512);
            glfwGetError(buffer);
            System.out.println(buffer.getStringUTF8());
        }

        glfwSetKeyCallback(window, Input.handler);

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);

        GL.createCapabilities();
        GLUtil.setupDebugMessageCallback();

        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(2);

            glfwGetWindowSize(window, width, height);
            resizeCallback.invoke(window, width.get(), height.get());
        }

        glfwSetWindowSizeCallback(window, resizeCallback);

        initialized = true;
    }

    int vao;
    int vbo;
    int ebo;

    float[] vertices = {
            0.5f,  0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            -0.5f,  0.5f, 0.0f
    };

    int[] indices = {
            0, 1, 3,
            1, 2, 3
    };

    public void Run() {
        if (!initialized) {
            throw new RuntimeException("Initializing the window failed");
        }
        glfwShowWindow(window);

        ShaderLibrary.getInstance().Load("default", "default");

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.length);
        vertexBuffer.put(0, vertices);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(vertexBuffer);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        ebo = glGenBuffers();
        IntBuffer indexBuffer = MemoryUtil.memAllocInt(indices.length);
        indexBuffer.put(0, indices);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(indexBuffer);

        glBindVertexArray(0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glClearColor(0.5f, 0.5f, 0.0f, 1.0f);

        Loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public void Loop() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

            try {
                ShaderLibrary.getInstance().Use("default");
                glBindVertexArray(vao);
                glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
                glBindVertexArray(0);
            } catch(Exception ignored) {}

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
