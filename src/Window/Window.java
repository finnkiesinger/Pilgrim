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
        glEnable(GL_CULL_FACE);
        glEnable(GL_MULTISAMPLE);

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
    int tex;

    int texture1, texture2, texture3;

    float[] vertices = {
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f
    };

    float[] textures = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f
    };


    float[] texCoords = {
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f
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

        ShaderLibrary.Instance().Load("default", "default");

        texture1 = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        IntBuffer width = MemoryUtil.memAllocInt(1);
        IntBuffer height = MemoryUtil.memAllocInt(1);
        IntBuffer channels = MemoryUtil.memAllocInt(1);

        stbi_set_flip_vertically_on_load(true);

        String path = ResourceLoader.GetPath("textures/concrete.png");

        ByteBuffer data = stbi_load(path, width, height, channels, 4);
        if (data != null) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
            glGenerateMipmap(GL_TEXTURE_2D);

            stbi_image_free(data);
        }

        memFree(width);
        memFree(height);
        memFree(channels);

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.length);
        vertexBuffer.put(0, vertices);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        MemoryUtil.memFree(vertexBuffer);

        tex = glGenBuffers();
        FloatBuffer texBuffer = MemoryUtil.memAllocFloat(textures.length);
        texBuffer.put(0, textures);
        glBindBuffer(GL_ARRAY_BUFFER, tex);
        glBufferData(GL_ARRAY_BUFFER, texBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);
        memFree(texBuffer);
/*
        ebo = glGenBuffers();
        IntBuffer indexBuffer = MemoryUtil.memAllocInt(indices.length);
        indexBuffer.put(0, indices);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(indexBuffer);
 */

        glBindVertexArray(0);
        //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

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

            Matrix4f projection = new Matrix4f().perspective(toRadians(45.0f), width / height, 0.1f, 100.f);
            Matrix4f model = new Matrix4f();

            game.Update();

            try {
                ShaderLibrary.Instance().Use("default");
                Shader shader = ShaderLibrary.Instance().GetActive();
                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, texture1);
                glBindVertexArray(vao);
                shader.SetMatrix4("model", model);
                shader.SetMatrix4("view", Camera.GetLookAt());
                shader.SetMatrix4("projection", projection);
                shader.SetInt("hovered", hovered ? 1 : 0);
                glDrawArrays(GL_TRIANGLES, 0, 36);
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
