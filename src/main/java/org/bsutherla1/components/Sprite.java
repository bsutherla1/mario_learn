package org.bsutherla1.components;

import org.bsutherla1.renderer.Texture;
import org.joml.Vector2f;

public class Sprite {

    private Texture texture;
    private Vector2f[] texCoords;

    public Sprite(Texture texture) {
        this(texture, new Vector2f[]{
                new Vector2f(1, 0),
                new Vector2f(1, 1),
                new Vector2f(0, 1),
                new Vector2f(0, 0),

        });
    }

    public Sprite(Texture texture, Vector2f[] texCoords) {
        this.texture = texture;
        this.texCoords = texCoords;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTexCoords() {
        return texCoords;
    }
}
