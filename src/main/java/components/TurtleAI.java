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
import util.AssetPool;

public class TurtleAI extends Component {
    private transient boolean goingRight = false;
    private transient Rigidbody2D rb;
    private transient float walkSpeed = 0.6f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);
    private transient boolean onGround = false;
    private transient boolean isDead = false;
    private transient boolean isMoving = false;
    private transient StateMachine stateMachine;
    private float movingDebounce = 0.32f;

    @Override
    public void start() {
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb = gameObject.getComponent(Rigidbody2D.class);
        this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
    }

    @Override
    public void update(float dt) {
        movingDebounce -= dt;
        Camera camera = Window.getScene().camera();
        if (this.gameObject.transform.position.x > camera.position.x + camera.projectionSize.x * camera.getZoom()) {
            return;
        }

        if (!isDead || isMoving) {
            if (goingRight) {
                velocity.x = walkSpeed;
            } else {
                velocity.x = -walkSpeed;
            }
        } else {
            velocity.x = 0;
        }

        checkOnGround();
        if (onGround) {
            this.acceleration.y = 0;
            this.velocity.y = 0;
        } else {
            this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
        }
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
        this.rb.setVelocity(velocity);

        if (this.gameObject.transform.position.x < Window.getScene().camera().position.x - 0.5f || this.gameObject.transform.position.y < 0.0f) {
            this.gameObject.destroy();
        }
    }

    public void checkOnGround() {
        Vector2f raycastBegin = new Vector2f(this.gameObject.transform.position);
        float innerPlayerWidth = 0.25f* 0.7f;
        raycastBegin.sub(innerPlayerWidth / 2.0f, 0.0f);
        float yVal = -0.2f;
        Vector2f raycastEnd = new Vector2f(raycastBegin).add(0.0f, yVal);

        RaycastInfo info = Window.getPhysics().raycast(gameObject, raycastBegin, raycastEnd);

        Vector2f raycast2Begin = new Vector2f(raycastBegin).add(innerPlayerWidth, 0.0f);
        Vector2f raycast2End = new Vector2f(raycastEnd).add(innerPlayerWidth, 0.0f);
        RaycastInfo info2 = Window.getPhysics().raycast(gameObject, raycast2Begin, raycast2End);

        onGround = (info.hit && info.hitObject != null && info.hitObject.getComponent(Ground.class) != null) ||
                (info2.hit && info2.hitObject != null && info2.hitObject.getComponent(Ground.class) != null);

        //DebugDraw.addLine2D(raycastBegin, raycastEnd, new Vector3f(1, 0, 0));
        //DebugDraw.addLine2D(raycast2Begin, raycast2End);
    }

    public void stomp() {
        this.isDead = true;
        this.isMoving = false;
        this.velocity.zero();
        this.rb.setVelocity(new Vector2f());
        this.rb.setAngularVelocity(0.0f);
        this.rb.setGravityScale(0.0f);
        this.stateMachine.trigger("squashMe");
        AssetPool.getSound("assets/sounds/bump.ogg").play();
    }

    public boolean isAlive() {
        return !this.isDead;
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        GoombaAI goombaAI = obj.getComponent(GoombaAI.class);
        if (goombaAI != null) {
            return;
        }

        PlayerController playerController = obj.getComponent(PlayerController.class);
        if (playerController != null) {
            if (!isDead && !playerController.isDead() && !playerController.isHurtInvincible() && contactNormal.y > 0.58f) {
                playerController.enemyBounce();
                stomp();
                walkSpeed *= 3.0f;
            } else if (movingDebounce < 0 && !playerController.isDead() && !playerController.isInvincible() &&
                    (isMoving || !isDead) && contactNormal.y < 0.58f) {
                playerController.die();
            } else if (!playerController.isDead() && !playerController.isHurtInvincible()) {
                if (isDead && contactNormal.y > 0.58f) {
                    playerController.enemyBounce();
                    isMoving = !isMoving;
                    goingRight = contactNormal.x < 0;
                } else if (isDead && !isMoving) {
                    isMoving = true;
                    goingRight = contactNormal.x < 0;
                    movingDebounce = 0.32f;
                }
            }
        } else if (Math.abs(contactNormal.y) < 0.1f && !obj.isDead()) {
            goingRight = contactNormal.x < 0;
            AssetPool.getSound("assets/sounds/bump.ogg").play();
        }

        if (obj.getComponent(Fireball.class) != null) {
            stomp();
            obj.getComponent(Fireball.class).kill();
        }
    }

    @Override
    public void preSolve(GameObject obj, Contact contact, Vector2f contactNormal) {
        GoombaAI goomba = obj.getComponent(GoombaAI.class);
        if (isDead && isMoving && goomba != null) {
            goomba.stomp(false);
            contact.setEnabled(false);
            AssetPool.getSound("assets/sounds/kick.ogg").play();
        }
    }
}
