package org.bsutherla1.jade;

import java.util.HashSet;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;

    private final HashSet<Integer> pressedKeys = new HashSet<>();

    private KeyListener() {}

    public static KeyListener get() {
        if (instance == null)
            instance = new KeyListener();

        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().pressedKeys.add(key);
        } else if (action == GLFW_RELEASE) {
            get().pressedKeys.remove(key);
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().pressedKeys.contains(keyCode);
    }
}
