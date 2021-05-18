package components;

import jade.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.CircleCollider;
import physics2d.components.Rigidbody2D;
import util.AssetPool;

public class Mushroom extends Component {

    private transient boolean goingRight = true;
    private transient Rigidbody2D rb;
    private transient Vector2f speed = new Vector2f(1.0f, 0.0f);
    private transient float maxSpeed = 0.8f;

    @Override
    public void start() {
        this.rb = gameObject.getComponent(Rigidbody2D.class);
        AssetPool.getSound("assets/sounds/powerup_appears.ogg").play();
    }

    @Override
    public void update(float dt) {
        if (goingRight && Math.abs(rb.getVelocity().x) < maxSpeed) {
            rb.addVelocity(speed);
        } else if (!goingRight && Math.abs(rb.getVelocity().x) < maxSpeed) {
            rb.addVelocity(new Vector2f(-speed.x, speed.y));
        }

        this.gameObject.getComponent(CircleCollider.class).editorUpdate(dt);
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact) {
        if (contact.m_manifold.localNormal.y < 0.3f) {
            goingRight = contact.m_manifold.localNormal.x > 0;
        }

        PlayerController playerController = obj.getComponent(PlayerController.class);
        if (playerController != null) {
            contact.setEnabled(false);
            playerController.powerup();
            this.gameObject.destroy();
        }
    }
}
