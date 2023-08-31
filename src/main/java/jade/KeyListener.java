package jade;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[GLFW_KEY_LAST + 1];
    private boolean keyBeginPress[] = new boolean[GLFW_KEY_LAST + 1];

    private KeyListener() {

    }

    public static void endFrame() {
        Arrays.fill(get().keyBeginPress, false);
    }

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key <= GLFW_KEY_LAST && key >= 0) {
            if (action == GLFW_PRESS) {
                get().keyPressed[key] = true;
                get().keyBeginPress[key] = true;
            } else if (action == GLFW_RELEASE) {
                get().keyPressed[key] = false;
                get().keyBeginPress[key] = false;
            }
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        if (keyCode <= GLFW_KEY_LAST && keyCode >= 0) {
            return get().keyPressed[keyCode];
        }

        return false;
    }

    public static boolean keyBeginPress(int keyCode) {
        if (keyCode <= GLFW_KEY_LAST && keyCode >= 0) {
            return get().keyBeginPress[keyCode];
        }

        return false;
    }
}
