package components;

import jade.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import util.AssetPool;

public class Coin extends Component {
    private Vector2f topY;
    private float coinSpeed = 1.4f;
    private transient boolean playAnim = false;

    @Override
    public void start() {
        topY = new Vector2f(this.gameObject.transform.position.y).add(0, 0.5f);
    }

    @Override
    public void update(float dt) {
        if (playAnim) {
            if (this.gameObject.transform.position.y < topY.y) {
                this.gameObject.transform.position.y += dt * coinSpeed;
                this.gameObject.transform.scale.x -= (0.5f * dt) % -1.0f;
            } else {
                gameObject.destroy();
            }
        }
    }

    @Override
    public void preSolve(GameObject obj, Contact contact, Vector2f contactNormal) {
        if (obj.getComponent(PlayerController.class) != null) {
            AssetPool.getSound("assets/sounds/coin.ogg").play();
            playAnim = true;
            contact.setEnabled(false);
        }
    }
}
