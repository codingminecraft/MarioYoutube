package components;

import jade.GameObject;
import jade.KeyListener;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.Rigidbody2D;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerController extends Component {
    public float walkSpeed = 1.0f;
    public float jumpBoost = 0.04f;
    public float maxWalkSpeed = 1.0f;

    private transient Rigidbody2D rb;
    private transient StateMachine stateMachine;
    private transient float playerWidth = 0.25f;
    private transient int onGround = 0;
    private transient float debounceBounce = 0.0f;
    private float bounceHoldTime = 0.15f;
    private transient float offGroundDebounce = 0.0f;
    private transient float offGroundDebounceTime = 0.1f;

    @Override
    public void start() {
        this.rb = gameObject.getComponent(Rigidbody2D.class);
        this.stateMachine = gameObject.getComponent(StateMachine.class);
    }

    @Override
    public void update(float dt) {
        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT) || KeyListener.isKeyPressed(GLFW_KEY_D)) {
            this.gameObject.transform.scale.x = playerWidth;
            if (Math.abs(this.rb.getVelocity().x) < maxWalkSpeed) {
                this.rb.addVelocity(new Vector2f(walkSpeed, 0.0f));
            }

            if (this.rb.getVelocity().x < 0) {
                this.stateMachine.trigger("switchDirection");
            } else {
                this.stateMachine.trigger("startRunning");
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) || KeyListener.isKeyPressed(GLFW_KEY_A)) {
            this.gameObject.transform.scale.x = -playerWidth;
            if (Math.abs(this.rb.getVelocity().x) < maxWalkSpeed) {
                this.rb.addVelocity(new Vector2f(-walkSpeed, 0.0f));
            }

            if (this.rb.getVelocity().x > 0) {
                this.stateMachine.trigger("switchDirection");
            } else {
                this.stateMachine.trigger("startRunning");
            }
        } else if (Math.abs(this.rb.getVelocity().x) < 0.1f) {
            this.stateMachine.trigger("stopRunning");
        }

        if ((debounceBounce >= 0 || onGround > 0) && KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            this.rb.addImpulse(new Vector2f(0.0f, jumpBoost));
            debounceBounce -= dt;
            AssetPool.getSound("assets/sounds/jump-small.ogg").play();
        }

        if (onGround > 0 && debounceBounce != bounceHoldTime) {
            debounceBounce = bounceHoldTime;
        }

        if (onGround == 0) {
            offGroundDebounce -= dt;
            if (offGroundDebounce < 0) {
                this.stateMachine.trigger("jump");
                offGroundDebounce = offGroundDebounceTime;
            }
        }
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact) {
        if (obj.getComponent(Ground.class) != null && contact.m_manifold.localNormal.y > 0.4f) {
            onGround++;
            this.stateMachine.trigger("stopJumping");
        }
    }

    @Override
    public void endCollision(GameObject obj, Contact contact) {
        if (obj.getComponent(Ground.class) != null && contact.m_manifold.localNormal.y > 0.4f) {
            onGround--;
        }
    }
}
