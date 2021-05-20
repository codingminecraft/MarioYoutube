package components;

import jade.GameObject;
import jade.KeyListener;
import jade.Window;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.RaycastInfo;
import physics2d.RaycastInfoCallback;
import physics2d.components.PillboxCollider;
import physics2d.components.Rigidbody2D;
import renderer.DebugDraw;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerController extends Component {
    private enum PlayerState {
        Small,
        Big,
        Fire,
        Invincible
    }

    public float walkSpeed = 1.0f;
    public float jumpBoost = 0.23f;
    public float maxWalkSpeed = 1.0f;
    public boolean onGround = false;

    private PlayerState playerState = PlayerState.Small;
    private transient Rigidbody2D rb;
    private transient StateMachine stateMachine;
    private transient float bigJumpBoostFactor = 1.05f;
    private transient float playerWidth = 0.25f;
    private transient int jumpTime = 0;
    private transient float yJumpSpeed = 0.0f;

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

        checkOnGround();
        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE) || (jumpTime < 0 && !onGround)) {
            if (jumpTime < 0) {
                jumpTime++;
            } else if (onGround) {
                AssetPool.getSound("assets/sounds/jump-small.ogg").play();
                yJumpSpeed = jumpBoost;
                jumpTime = 16;
            } else if (jumpTime > 0) {
                jumpTime--;
            }
            float yForce = jumpTime * yJumpSpeed;
            this.rb.addVelocity(new Vector2f(0, yForce));
        }

        if (!onGround) {
            stateMachine.trigger("jump");
        } else {
            stateMachine.trigger("stopJumping");
        }
    }

    public void checkOnGround() {
        // TODO: Do two raycasts towards the width of mario to check if he's on the ground
        Vector2f raycastBegin = this.gameObject.transform.position;
        float yVal = playerState == PlayerState.Small ? -0.14f : -0.24f;
        Vector2f raycastEnd = new Vector2f(raycastBegin).add(0.0f, yVal);

        RaycastInfo info = Window.getPhysics().raycast(gameObject, raycastBegin, raycastEnd);
        onGround = info.hit && info.hitObject != null && info.hitObject.getComponent(Ground.class) != null;

        //DebugDraw.addLine2D(raycastBegin, raycastEnd);
    }

    public void powerup() {
        if (playerState == PlayerState.Small) {
            playerState = PlayerState.Big;
            AssetPool.getSound("assets/sounds/powerup.ogg").play();
            gameObject.transform.scale.y = 0.42f;
            PillboxCollider pb = gameObject.getComponent(PillboxCollider.class);
            if (pb != null) {
                jumpBoost *= bigJumpBoostFactor;
                walkSpeed *= bigJumpBoostFactor;
                pb.setHeight(0.63f);
            }
        } else if (playerState == PlayerState.Big) {
            playerState = PlayerState.Fire;
            AssetPool.getSound("assets/sounds/powerup.ogg").play();
        }

        stateMachine.trigger("powerup");
    }

    public void goInvincible() {
        playerState = PlayerState.Invincible;
    }

    public boolean isSmall() {
        return this.playerState == PlayerState.Small;
    }

    public boolean isBig() {
        return this.playerState == PlayerState.Big;
    }
}