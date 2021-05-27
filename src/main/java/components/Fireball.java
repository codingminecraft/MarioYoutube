package components;

import jade.Camera;
import jade.GameObject;
import jade.Window;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector3f;
import physics2d.RaycastInfo;
import physics2d.components.Rigidbody2D;
import renderer.DebugDraw;

public class Fireball extends Component {
    public transient boolean goingRight = false;
    private transient Rigidbody2D rb;
    private transient float walkSpeed = 1.7f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);
    private transient boolean onGround = false;
    private transient float lifetime = 4.0f;

    private static int fireballCount = 0;

    public static boolean canSpawn() {
        return fireballCount < 4;
    }

    @Override
    public void start() {
        this.rb = gameObject.getComponent(Rigidbody2D.class);
        this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
        fireballCount++;
    }

    @Override
    public void update(float dt) {
        lifetime -= dt;
        if (lifetime < 0) {
            kill();
        }

        if (goingRight) {
            velocity.x = walkSpeed;
        } else {
            velocity.x = -walkSpeed;
        }

        checkOnGround();
        if (onGround) {
            this.acceleration.y = 1.5f;
            this.velocity.y = 2.5f;
        } else {
            this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
        }

        this.velocity.y += this.acceleration.y * dt;
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
        this.rb.setVelocity(velocity);
    }

    public void checkOnGround() {
        Vector2f raycastBegin = new Vector2f(this.gameObject.transform.position);
        float innerPlayerWidth = 0.25f * 0.7f;
        raycastBegin.sub(innerPlayerWidth / 2.0f, 0.0f);
        float yVal = -0.09f;
        Vector2f raycastEnd = new Vector2f(raycastBegin).add(0.0f, yVal);

        RaycastInfo info = Window.getPhysics().raycast(gameObject, raycastBegin, raycastEnd);

        Vector2f raycast2Begin = new Vector2f(raycastBegin).add(innerPlayerWidth, 0.0f);
        Vector2f raycast2End = new Vector2f(raycastEnd).add(innerPlayerWidth, 0.0f);
        RaycastInfo info2 = Window.getPhysics().raycast(gameObject, raycast2Begin, raycast2End);

        onGround = (info.hit && info.hitObject != null && info.hitObject.getComponent(Ground.class) != null) ||
                (info2.hit && info2.hitObject != null && info2.hitObject.getComponent(Ground.class) != null);
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (Math.abs(contactNormal.x) > 0.8f) {
            this.goingRight = contactNormal.x < 0;
        }
    }

    @Override
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (collidingObject.getComponent(PlayerController.class) != null || collidingObject.getComponent(Fireball.class) != null) {
            contact.setEnabled(false);
        }
    }

    public void kill() {
        fireballCount--;
        this.gameObject.destroy();
    }
}
