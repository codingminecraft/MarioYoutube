package components;

import jade.GameObject;
import jade.KeyListener;
import jade.MouseListener;
import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import renderer.DebugDraw;
import renderer.PickingTexture;
import scenes.Scene;
import util.Settings;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    GameObject holdingObject = null;
    private float debounceTime = 0.05f;
    private float debounce = debounceTime;

    private boolean boxSelectSet = false;
    private Vector2f boxSelectStart = new Vector2f();
    private Vector2f boxSelectEnd = new Vector2f();

    public void pickupObject(GameObject go) {
        if (this.holdingObject != null) {
            this.holdingObject.destroy();
        }
        this.holdingObject = go;
        this.holdingObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
        this.holdingObject.addComponent(new NonPickable());
        Window.getScene().addGameObjectToScene(go);
    }

    public void place() {
        GameObject newObj = holdingObject.copy();
        if (newObj.getComponent(StateMachine.class) != null) {
            newObj.getComponent(StateMachine.class).refreshTextures();
        }
        newObj.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1));
        newObj.removeComponent(NonPickable.class);
        Window.getScene().addGameObjectToScene(newObj);
    }

    @Override
    public void editorUpdate(float dt) {
        debounce -= dt;
        PickingTexture pickingTexture = Window.getImguiLayer().getPropertiesWindow().getPickingTexture();
        Scene currentScene = Window.getScene();

        if (holdingObject != null && debounce <= 0.0f) {
            float x = MouseListener.getWorldX();
            float y = MouseListener.getWorldY();
            holdingObject.transform.position.x = ((int)Math.floor(x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH) + Settings.GRID_WIDTH / 2.0f;
            holdingObject.transform.position.y = ((int)Math.floor(y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT) + Settings.GRID_HEIGHT / 2.0f;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
                debounce = debounceTime;
            }

            if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
                holdingObject.destroy();
                holdingObject = null;
            }
        } else if (!MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();
            int gameObjectId = pickingTexture.readPixel(x, y);
            GameObject pickedObj = currentScene.getGameObject(gameObjectId);
            if (pickedObj != null && pickedObj.getComponent(NonPickable.class) == null) {
                Window.getImguiLayer().getPropertiesWindow().setActiveGameObject(pickedObj);
            } else if (pickedObj == null && !MouseListener.isDragging()) {
                Window.getImguiLayer().getPropertiesWindow().clearSelected();
            }
            this.debounce = 0.2f;
        } else if (MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            if (!boxSelectSet) {
                Window.getImguiLayer().getPropertiesWindow().clearSelected();
                boxSelectStart = MouseListener.getScreen();
                boxSelectSet = true;
            }
            boxSelectEnd = MouseListener.getScreen();
            Vector2f boxSelectStartWorld = MouseListener.screenToWorld(boxSelectStart);
            Vector2f boxSelectEndWorld = MouseListener.screenToWorld(boxSelectEnd);
            Vector2f halfSize =
                    (new Vector2f(boxSelectEndWorld).sub(boxSelectStartWorld)).mul(0.5f);
            DebugDraw.addBox2D(
                    (new Vector2f(boxSelectStartWorld)).add(halfSize),
                    new Vector2f(halfSize).mul(2.0f),
                    0.0f);
        } else if (boxSelectSet) {
            boxSelectSet = false;
            int screenStartX = (int)boxSelectStart.x;
            int screenStartY = (int)boxSelectStart.y;
            int screenEndX = (int)boxSelectEnd.x;
            int screenEndY = (int)boxSelectEnd.y;
            boxSelectStart.zero();
            boxSelectEnd.zero();

            if (screenEndX < screenStartX) {
                int tmp = screenStartX;
                screenStartX = screenEndX;
                screenEndX = tmp;
            }
            if (screenEndY < screenStartY) {
                int tmp = screenStartY;
                screenStartY = screenEndY;
                screenEndY = tmp;
            }

            float[] gameObjectIds = pickingTexture.readPixels(
                    new Vector2i(screenStartX, screenStartY),
                    new Vector2i(screenEndX, screenEndY)
            );
            Set<Integer> uniqueGameObjectIds = new HashSet<>();
            for (float objId : gameObjectIds) {
                uniqueGameObjectIds.add((int)objId);
            }

            for (Integer gameObjectId : uniqueGameObjectIds) {
                GameObject pickedObj = Window.getScene().getGameObject(gameObjectId);
                if (pickedObj != null && pickedObj.getComponent(NonPickable.class) == null) {
                    Window.getImguiLayer().getPropertiesWindow().addActiveGameObject(pickedObj);
                }
            }
        }
    }
}
