package org.bsutherla1.jade;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene() {
        System.out.println("Inside Level Editor scene.");
    }

    @Override
    public void update(float dt) {

        if (!changingScene && KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0)
        {
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
        }
        else if (changingScene)
        {
            Window.changeScene(1);
        }
    }
}
