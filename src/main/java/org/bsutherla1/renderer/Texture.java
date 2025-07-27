package org.bsutherla1.renderer;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

    private final int texID;
    private final int width, height;

    public Texture(String filepath) throws IOException {

        // Generate texture on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // Set texture parameters
        // Repeat the image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // When stretching the image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // When shrinking the image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if (image == null ) {
            throw new IOException("Error: Could not load image '" + filepath + "'");
        }

        this.width = width.get(0);
        this.height = height.get(0);

        if (channels.get(0) < 3 || channels.get(0) > 4) {
            stbi_image_free(image);
            throw new IOException("Error: Image '" + filepath + "' has "
                    + channels.get() + " color channels. Images must have either 3 or 4 color channels.");
        }

        int channelType = channels.get(0) == 4 ? GL_RGBA : GL_RGB;
        glTexImage2D(GL_TEXTURE_2D, 0, channelType, width.get(0), height.get(0),
                0, channelType, GL_UNSIGNED_BYTE, image);

        stbi_image_free(image);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unBind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }
}
