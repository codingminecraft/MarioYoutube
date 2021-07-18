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
                            && isEntrance) {
                        playerEntering = true;
                    }
                    break;
                case Left:
                    if ((KeyListener.isKeyPressed(GLFW_KEY_RIGHT)
                            || KeyListener.isKeyPressed(GLFW_KEY_D))
                            && isEntrance) {
                        playerEntering = true;
                    }
                    break;
                case Right:
                    if ((KeyListener.isKeyPressed(GLFW_KEY_LEFT)
                            || KeyListener.isKeyPressed(GLFW_KEY_A))
                            && isEntrance) {
                        playerEntering = true;
                    }
                    break;
                case Down:
                    if ((KeyListener.isKeyPressed(GLFW_KEY_UP)
                            || KeyListener.isKeyPressed(GLFW_KEY_W))
                            && isEntrance) {
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

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = collidingObject.getComponent(PlayerController.class);
        if (playerController != null) {
            switch (direction) {
                case Up:
                    if (contactNormal.y < entranceVectorTolerance) {
                        return;
                    }
                    break;
                case Right:
                    if (contactNormal.x < entranceVectorTolerance) {
                        return;
                    }
                    break;
                case Down:
                    if (contactNormal.y > -entranceVectorTolerance) {
                        return;
                    }
                    break;
                case Left:
                    if (contactNormal.x > - entranceVectorTolerance) {
                        return;
                    }
                    break;
            }
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
