package org.bsutherla1.renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {

    private final static String REG_EX = "(#type)( )+([a-zA-Z]+)";

    private int shaderProgramID;

    private final String filepath;
    private String vertexSource;
    private String fragmentSource;
    private boolean beingUsed = false;

    public Shader(String filepath) {
        this.filepath = filepath;

        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            vertexSource = getShaderSource(source, Type.Vertex);
            fragmentSource = getShaderSource(source, Type.Fragment);
        }
        catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }
    }

    private enum Type {
        Vertex("vertex", GL_VERTEX_SHADER),
        Fragment("fragment", GL_FRAGMENT_SHADER);

        final String s; final int t;
        private Type(String s, int t) { this.s = s; this.t = t;}
    }

    private String getShaderSource(String source, Type type) throws IOException {
        if (!source.contains(type.s))
            throw new IOException("Missing source '" + type.s + "' shader.");

        int index, eol = 0, iter = 0;
        String pattern = "";

        do {
            index = source.indexOf("#type", eol) + "#type ".length();
            eol = source.indexOf("\r\n", index);
            pattern = source.substring(index, eol);
            iter++;
        } while (!pattern.contains(type.s));

        return source.split(REG_EX)[iter];
    }

    public void compileAndLinkShaders() {
        int vertexID, fragmentID;

        // Compile Vertex and Fragment Shaders
        vertexID = compileShader(Type.Vertex, vertexSource);
        fragmentID = compileShader(Type.Fragment, fragmentSource);
        linkShaders(vertexID, fragmentID);
    }

    private int compileShader(Type type, String shaderSource) {
        int shaderID = glCreateShader(type.t);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            int len = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\t" + type.s + " shader compilation failed.");
            System.out.println(glGetShaderInfoLog(shaderID, len));
            assert false : "";
        }

        return shaderID;
    }

    private void linkShaders(int vertexID, int fragmentID) {
        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        if (glGetProgrami(shaderProgramID, GL_LINK_STATUS) == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use() {
        if (beingUsed)
            return;

        glUseProgram(shaderProgramID);
        beingUsed = true;
    }

    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }

    // Quick and dirty update to test Camera
    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector2f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, val);
    }
}
