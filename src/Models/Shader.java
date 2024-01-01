package Models;

import static org.lwjgl.opengl.GL33.*;

public class Shader {
    private int ID;

    public Shader(String vertexPath, String fragmentPath) {

    }

    public void Use() {
        glUseProgram(ID);
    }
}
