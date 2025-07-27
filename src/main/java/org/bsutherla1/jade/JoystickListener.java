package org.bsutherla1.jade;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class JoystickListener {
    private final static JoystickListener[] instances = new JoystickListener[GLFW_JOYSTICK_LAST + 1];
    private final int jid;
    private final String name;

    private FloatBuffer axes;
    private ByteBuffer buttons;
    private ByteBuffer hats;

    private JoystickListener(int jid) {
        this.jid = jid;
        name = glfwGetJoystickName(jid);
    }

    private String getName() {
        return name;
    }

    private void pullEvents() {
        axes = glfwGetJoystickAxes(jid);
        buttons = glfwGetJoystickButtons(jid);
        hats = glfwGetJoystickHats(jid);
    }

    public float getAxisValue(int axis) {
        if (axes == null || axis < 0 || axis >= axes.capacity())
            return 0.0f;

        return axes.get(axis);
    }

    public boolean isButtonPressed(int button) {
        if (buttons == null || button < 0 || button >= buttons.capacity())
            return false;

        return buttons.get(button) == GLFW_PRESS;
    }

    public boolean isButtonReleased(int button) {
        return !isButtonPressed(button);
    }

    public boolean isHatCentered(int hat) {
        if (hats == null || hat < 0 || hat >= hats.capacity())
            return true;

        return hats.get(hat) == GLFW_HAT_CENTERED;
    }

    public boolean isHatUpPressed(int hat) {
        if (hats == null || hat < 0 || hat >= hats.capacity())
            return false;

        return (hats.get(hat) & GLFW_HAT_UP) != 0;
    }

    public boolean isHatDownPressed(int hat) {
        if (hats == null || hat < 0 || hat >= hats.capacity())
            return false;

        return (hats.get(hat) & GLFW_HAT_DOWN) != 0;
    }

    public boolean isHatLeftPressed(int hat) {
        if (hats == null || hat < 0 || hat >= hats.capacity())
            return false;

        return (hats.get(hat) & GLFW_HAT_LEFT) != 0;
    }

    public boolean isHatRightPressed(int hat) {
        if (hats == null || hat < 0 || hat >= hats.capacity())
            return false;

        return (hats.get(hat) & GLFW_HAT_RIGHT) != 0;
    }

    public boolean isHatUpLeftPressed(int hat) {
        return isHatUpPressed(hat) && isHatLeftPressed(hat);
    }

    public boolean isHatUpRightPressed(int hat) {
        return isHatUpPressed(hat) && isHatRightPressed(hat);
    }

    public boolean isHatDownLeftPressed(int hat) {
        return isHatDownPressed(hat) && isHatLeftPressed(hat);
    }

    public boolean isHatDownRightPressed(int hat) {
        return isHatDownPressed(hat) && isHatRightPressed(hat);
    }

    public static void init() {
        for (int jid = GLFW_JOYSTICK_1; jid <= GLFW_JOYSTICK_LAST; jid++) {
            if (glfwJoystickPresent(jid)) {
                joystickCallback(jid, GLFW_CONNECTED);
            }
        }
    }

    public static void joystickCallback(int jid, int event) {
        if (event == GLFW_CONNECTED) {
            instances[jid] = new JoystickListener(jid);
            System.out.println("Joystick ID: " + jid + " " + instances[jid].getName() + " connected.");
        }
        if (event == GLFW_DISCONNECTED) {
            System.out.println("Joystick ID: " + jid + " " + instances[jid].getName() + " disconnected.");
            instances[jid] = null;
        }
    }

    private static void checkJoystickId(int jid) {
        if (jid >= GLFW_JOYSTICK_1 && jid <= GLFW_JOYSTICK_LAST)
            return;
        throw new ArrayIndexOutOfBoundsException("Invalid joystick id provided.");
    }

    public static void pullEvents(int jid) {
        checkJoystickId(jid);

        if (instances[jid] == null)
            return;
        instances[jid].pullEvents();
    }

    public static float getAxisValue(int jid, int axis) {
        checkJoystickId(jid);

        if (instances[jid] == null)
            return 0.0f;
        return instances[jid].getAxisValue(axis);
    }

    public static boolean isButtonPressed(int jid, int button) {
        checkJoystickId(jid);

        if (instances[jid] == null)
            return false;
        return instances[jid].isButtonPressed(button);
    }

    public static boolean isHatCentered(int jid, int hat) {
        checkJoystickId(jid);

        if (instances[jid] == null)
            return true;
        return instances[jid].isHatCentered(hat);
    }

    public static boolean isHatUpPressed(int jid, int hat) {
        checkJoystickId(jid);

        if (instances[jid] == null)
            return false;
        return instances[jid].isHatUpPressed(hat);
    }

    public static boolean isHatDownPressed(int jid, int hat) {
        checkJoystickId(jid);

        if (instances[jid] == null)
            return false;
        return instances[jid].isHatDownPressed(hat);
    }

    public static boolean isHatLeftPressed(int jid, int hat) {
        checkJoystickId(jid);

        if (instances[jid] == null)
            return false;
        return instances[jid].isHatLeftPressed(hat);
    }

    public static boolean isHatRightPressed(int jid, int hat) {
        checkJoystickId(jid);

        if (instances[jid] == null)
            return false;
        return instances[jid].isHatRightPressed(hat);
    }

    public static boolean isHatUpLeftPressed(int jid, int hat) {
        checkJoystickId(jid);

        if (instances[jid] == null)
            return false;
        return instances[jid].isHatUpLeftPressed(hat);
    }

    public static boolean isHatUpRightPressed(int jid, int hat) {
        checkJoystickId(jid);

        if (instances[jid] == null)
            return false;
        return instances[jid].isHatUpRightPressed(hat);
    }

    public static boolean isHatDownLeftPressed(int jid, int hat) {
        checkJoystickId(jid);

        if (instances[jid] == null)
            return false;
        return instances[jid].isHatDownLeftPressed(hat);
    }

    public static boolean isHatDownRightPressed(int jid, int hat) {
        checkJoystickId(jid);

        if (instances[jid] == null)
            return false;
        return instances[jid].isHatDownRightPressed(hat);
    }
}

