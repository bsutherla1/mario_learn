package org.bsutherla1.util;

import org.bsutherla1.renderer.Shader;
import org.bsutherla1.renderer.Texture;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();

    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        if (shaders.containsKey(file.getAbsolutePath())) {
            return shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourceName);
            shader.compileAndLinkShaders();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
        if (textures.containsKey(file.getAbsolutePath())) {
            return textures.get(file.getAbsolutePath());
        } else {
            try {
                Texture texture = new Texture(resourceName);
                AssetPool.textures.put(file.getAbsolutePath(), texture);
                return texture;
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
