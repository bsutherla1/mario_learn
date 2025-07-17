package org.bsutherla1.jade;

import org.bsutherla1.components.FontRenderer;
import org.bsutherla1.components.SpriteRenderer;
import org.bsutherla1.renderer.Shader;
import org.bsutherla1.renderer.Texture;
import org.bsutherla1.util.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private float[] vertexArray = {
        // position                 // color                    // UV Coordinates
        100f,    0f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f,     1, 1, // Bottom right
          0f,  100f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f,     0, 0, // Top left
        100f,  100f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f,     1, 0, // Top right
          0f,    0f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f,     0, 1, // Bottom left
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
        2, 1, 0,    // Top right triangle
        0, 1, 3,    // Bottom left triangle
    };

    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    private Texture testTexture;

    private GameObject testObj;
    private boolean firstTime = false;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        System.out.println("Creating 'test object'");
        this.testObj = new GameObject("test object");
        this.testObj.addComponent(new SpriteRenderer());
        this.testObj.addComponent(new FontRenderer());
        this.addGameObjectToScene(this.testObj);

        this.camera = new Camera(new Vector2f(-200, -300));
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compileAndLinkShaders();

        try {
            testTexture = new Texture("assets/images/testImage.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of verticies
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        //camera.position.x -= dt * 50.0f;
        //camera.position.y -= dt * 20.0f;

        // Bind shader program
        defaultShader.use();

        // Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        // Quick and dirty update to test camera
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        // Bind VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

        if (!firstTime) {
            System.out.println("Creating gameObject!");
            GameObject go = new GameObject("Game Test 2");
            go.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(go);
            firstTime = true;
        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
    }
}
