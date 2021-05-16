package components;

import jade.Camera;
import jade.GameObject;
import jade.Window;
import org.joml.Vector4f;

public class GameCamera extends Component {
    private transient GameObject player;
    private transient Camera gameCamera;
    private transient float highestX = Float.MIN_VALUE;

    public GameCamera(Camera gameCamera) {
        this.gameCamera = gameCamera;
    }

    @Override
    public void start() {
        this.player = Window.getScene().findGameObjectWith(PlayerController.class);
        this.gameCamera.clearColor = new Vector4f(92.0f / 255.0f, 148.0f / 255.0f, 252.0f / 255.0f, 1.0f);
    }

    @Override
    public void update(float dt) {
        if (player != null) {
            gameCamera.position.x = Math.max(player.transform.position.x - 2.5f, highestX);
            highestX = Math.max(highestX, gameCamera.position.x);
        }
    }
}
