package components;

import jade.Direction;
import jade.GameObject;
import jade.KeyListener;
import jade.Window;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class Pipe extends Component {
    private Direction direction;
    private String connectingPipeName = "";
    private boolean isEntrance = false;
    private transient GameObject connectingPipe = null;
    private transient float entranceVectorTolerance = 0.6f;
    private transient PlayerController collidingPlayer = null;

    public Pipe(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void start() {
        connectingPipe = Window.getScene().getGameObject(connectingPipeName);
    }

    @Override
    public void update(float dt) {
        if (connectingPipe == null) {
            return;
        }

        if (collidingPlayer != null) {
            boolean playerEntering = false;
            switch (direction) {
                case Up:
                    if ((KeyListener.isKeyPressed(GLFW_KEY_DOWN)
                            || KeyListener.isKeyPressed(GLFW_KEY_S))
                            && isEntrance
                            && playerAtEntrance()) {
                        playerEntering = true;
                    }
                    break;
                case Left:
                    if ((KeyListener.isKeyPressed(GLFW_KEY_RIGHT)
                            || KeyListener.isKeyPressed(GLFW_KEY_D))
                            && isEntrance
                            && playerAtEntrance()) {
                        playerEntering = true;
                    }
                    break;
                case Right:
                    if ((KeyListener.isKeyPressed(GLFW_KEY_LEFT)
                            || KeyListener.isKeyPressed(GLFW_KEY_A))
                            && isEntrance
                            && playerAtEntrance()) {
                        playerEntering = true;
                    }
                    break;
                case Down:
                    if ((KeyListener.isKeyPressed(GLFW_KEY_UP)
                            || KeyListener.isKeyPressed(GLFW_KEY_W))
                            && isEntrance
                            && playerAtEntrance()) {
                        playerEntering = true;
                    }
                    break;
            }

            if (playerEntering) {
                collidingPlayer.setPosition(
                        getPlayerPosition(connectingPipe)
                );
                AssetPool.getSound("assets/sounds/pipe.ogg").play();
            }
        }
    }

    public boolean playerAtEntrance() {
        if (collidingPlayer == null) {
            return false;
        }

        Vector2f min = new Vector2f(gameObject.transform.position).
                sub(new Vector2f(gameObject.transform.scale).mul(0.5f));
        Vector2f max = new Vector2f(gameObject.transform.position).
                add(new Vector2f(gameObject.transform.scale).mul(0.5f));
        Vector2f playerMax = new Vector2f(collidingPlayer.gameObject.transform.position).
                add(new Vector2f(collidingPlayer.gameObject.transform.scale).mul(0.5f));
        Vector2f playerMin = new Vector2f(collidingPlayer.gameObject.transform.position).
                sub(new Vector2f(collidingPlayer.gameObject.transform.scale).mul(0.5f));

        switch (direction) {
            case Up:
                return playerMin.y >= max.y &&
                        playerMax.x > min.x &&
                        playerMin.x < max.x;
            case Down:
                return playerMax.y <= min.y &&
                        playerMax.x > min.x &&
                        playerMin.x < max.x;
            case Right:
                return playerMin.x >= max.x &&
                        playerMax.y > min.y &&
                        playerMin.y < max.y;
            case Left:
                return playerMin.x <= min.x &&
                        playerMax.y > min.y &&
                        playerMin.y < max.y;
        }

        System.out.println("HAHAHA");
        return false;
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = collidingObject.getComponent(PlayerController.class);
        if (playerController != null) {
            collidingPlayer = playerController;
        }
    }

    @Override
    public void endCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = collidingObject.getComponent(PlayerController.class);
        if (playerController != null) {
            collidingPlayer = null;
        }
    }

    private Vector2f getPlayerPosition(GameObject pipe) {
        Pipe pipeComponent = pipe.getComponent(Pipe.class);
        switch (pipeComponent.direction) {
            case Up:
                return new Vector2f(pipe.transform.position).add(0.0f, 0.5f);
            case Left:
                return new Vector2f(pipe.transform.position).add(-0.5f, 0.0f);
            case Right:
                return new Vector2f(pipe.transform.position).add(0.5f, 0.0f);
            case Down:
                return new Vector2f(pipe.transform.position).add(0.0f, -0.5f);
        }

        return new Vector2f();
    }
}
