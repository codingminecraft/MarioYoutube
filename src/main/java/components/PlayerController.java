package components;

import jade.GameObject;
import jade.KeyListener;
import jade.Prefabs;
import jade.Window;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import physics2d.RaycastInfo;
import physics2d.RaycastInfoCallback;
import physics2d.components.PillboxCollider;
import physics2d.components.Rigidbody2D;
import physics2d.enums.BodyType;
import renderer.DebugDraw;
import scenes.LevelEditorSceneInitializer;
import scenes.LevelSceneInitializer;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerController extends Component {
    private enum PlayerState {
        Small,
        Big,
        Fire,
        Invincible
    }

    public float walkSpeed = 1.9f;
    public float jumpBoost = 1.0f;
    public float jumpImpulse = 3.0f;
    public float slowDownForce = 0.05f;
    public Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);

    private PlayerState playerState = PlayerState.Small;
    public transient boolean onGround = false;
    private transient float groundDebounce = 0.0f;
    private transient float groundDebounceTime = 0.1f;
    private transient Rigidbody2D rb;
    private transient StateMachine stateMachine;
    private transient float bigJumpBoostFactor = 1.05f;
    private transient float playerWidth = 0.25f;
    private transient int jumpTime = 0;
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private transient boolean isDead = false;
    private transient int enemyBounce = 0;

    private transient boolean deadGoingUp = true;
    private transient float deadMaxHeight = 0;
    private transient float deadMinHeight = 0;
    private transient float hurtInvincibilityTimeLeft = 0.0f;
    private transient float hurtInvincibilityTime = 1.4f;
    private transient float blinkTime = 0.0f;
    private transient SpriteRenderer spr;

    private transient boolean playWinAnimation = false;
    private transient float timeToCastle = 4.5f;
    private transient float walkTime = 2.2f;
    private transient boolean sentEvent = false;

    @Override
    public void start() {
        this.rb = gameObject.getComponent(Rigidbody2D.class);
        this.spr = gameObject.getComponent(SpriteRenderer.class);
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb.setGravityScale(0.0f);
    }

    @Override
    public void update(float dt) {
        if (playWinAnimation) {
            checkOnGround();
            if (!onGround) {
                this.gameObject.transform.scale.x = -0.25f;
                this.gameObject.transform.position.y -= dt * 0.9f;
                this.stateMachine.trigger("stopRunning");
                this.stateMachine.trigger("stopJumping");
                timeToCastle = 4.5f;
                walkTime = 2.2f;
            } else {
                if (this.walkTime > 0) {
                    this.gameObject.transform.scale.x = 0.25f;
                    this.gameObject.transform.position.x += dt * 1.0f;
                    this.stateMachine.trigger("startRunning");
                }
                if (!AssetPool.getSound("assets/sounds/stage_clear.ogg").isPlaying()) {
                    AssetPool.getSound("assets/sounds/stage_clear.ogg").play();
                }
                timeToCastle -= dt;
                walkTime -= dt;

                if (timeToCastle <= 0) {
                    if (!sentEvent) {
                        EventSystem.notify(null, new Event(EventType.GameEngineStopPlay));
                    }
                }
            }
            return;
        }

        if (isDead) {
            if (this.gameObject.transform.position.y < deadMaxHeight && deadGoingUp) {
                this.gameObject.transform.position.y += dt * walkSpeed / 2.0f;
            } else if (this.gameObject.transform.position.y >= deadMaxHeight && deadGoingUp) {
                deadGoingUp = false;
            } else if (!deadGoingUp && gameObject.transform.position.y > deadMinHeight) {
                this.rb.setBodyType(BodyType.Kinematic);
                this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
                this.velocity.y += this.acceleration.y * dt;
                this.velocity.y = Math.max(Math.min(this.velocity.y, terminalVelocity.y), -terminalVelocity.y);
                this.rb.setVelocity(this.velocity);
                this.rb.setAngularVelocity(0);
            } else if (!deadGoingUp && gameObject.transform.position.y <= deadMinHeight) {
                Window.changeScene(new LevelSceneInitializer());
            }
            return;
        }

        if (hurtInvincibilityTimeLeft > 0) {
            hurtInvincibilityTimeLeft -= dt;
            blinkTime -= dt;

            if (blinkTime <= 0) {
                blinkTime = 0.2f;
                if (spr.getColor().w == 1) {
                    spr.setColor(new Vector4f(1, 1, 1, 0));
                } else {
                    spr.setColor(new Vector4f(1, 1, 1, 1));
                }
            }
        } else {
            if (spr.getColor().w == 0) {
                spr.setColor(new Vector4f(1, 1, 1, 1));
            }
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT) || KeyListener.isKeyPressed(GLFW_KEY_D)) {
            this.gameObject.transform.scale.x = playerWidth;
            this.acceleration.x = this.walkSpeed;

            if (this.velocity.x < 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x += slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) || KeyListener.isKeyPressed(GLFW_KEY_A)) {
            this.gameObject.transform.scale.x = -playerWidth;
            this.acceleration.x = -this.walkSpeed;

            if (this.velocity.x > 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x -= slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
        } else {
            this.acceleration.x = 0;
            if (this.velocity.x > 0) {
                this.velocity.x = Math.max(0, this.velocity.x - slowDownForce);
            } else if (this.velocity.x < 0) {
                this.velocity.x = Math.min(0, this.velocity.x + slowDownForce);
            }

            if (this.velocity.x == 0) {
                this.stateMachine.trigger("stopRunning");
            }
        }

        if (KeyListener.keyBeginPress(GLFW_KEY_E) && playerState == PlayerState.Fire && Fireball.canSpawn()) {
            Vector2f position = new Vector2f(this.gameObject.transform.position)
                    .add(this.gameObject.transform.scale.x > 0 ? new Vector2f(0.26f, 0) : new Vector2f(-0.26f, 0.0f));
            GameObject fireball = Prefabs.generateFireball(position);
            fireball.getComponent(Fireball.class).goingRight = this.gameObject.transform.scale.x > 0;
            Window.getScene().addGameObjectToScene(fireball);
        }

        checkOnGround();
        if ((KeyListener.isKeyPressed(GLFW_KEY_SPACE) && jumpTime > 0) || (onGround && KeyListener.isKeyPressed(GLFW_KEY_SPACE)) ||
                (groundDebounce > 0 && KeyListener.isKeyPressed(GLFW_KEY_SPACE))) {
            if ((onGround || groundDebounce > 0) && jumpTime == 0) {
                AssetPool.getSound("assets/sounds/jump-small.ogg").play();
                jumpTime = 28;
                this.velocity.y = jumpImpulse;
            } else if (jumpTime > 0) {
                jumpTime--;
                this.velocity.y = ((jumpTime / 2.2f) * jumpBoost);
            } else {
                this.velocity.y = 0;
            }
            groundDebounce = 0f;
        } else if (enemyBounce > 0) {
            enemyBounce--;
            this.velocity.y = ((enemyBounce / 2.2f) * jumpBoost);
        } else if (!onGround) {
            if (this.jumpTime > 0) {
                this.velocity.y *= 0.35f;
                this.jumpTime = 0;
            }
            groundDebounce -= dt;
            this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
        } else {
            this.velocity.y = 0;
            this.acceleration.y = 0;
            groundDebounce = groundDebounceTime;
        }

        this.velocity.x += this.acceleration.x * dt;
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
        this.rb.setVelocity(this.velocity);
        this.rb.setAngularVelocity(0);

        if (!onGround) {
            stateMachine.trigger("jump");
        } else {
            stateMachine.trigger("stopJumping");
        }
    }

    public void checkOnGround() {
        Vector2f raycastBegin = new Vector2f(this.gameObject.transform.position);
        float innerPlayerWidth = this.playerWidth * 0.6f;
        raycastBegin.sub(innerPlayerWidth / 2.0f, 0.0f);
        float yVal = playerState == PlayerState.Small ? -0.14f : -0.24f;
        Vector2f raycastEnd = new Vector2f(raycastBegin).add(0.0f, yVal);

        RaycastInfo info = Window.getPhysics().raycast(gameObject, raycastBegin, raycastEnd);

        Vector2f raycast2Begin = new Vector2f(raycastBegin).add(innerPlayerWidth, 0.0f);
        Vector2f raycast2End = new Vector2f(raycastEnd).add(innerPlayerWidth, 0.0f);
        RaycastInfo info2 = Window.getPhysics().raycast(gameObject, raycast2Begin, raycast2End);

        onGround = (info.hit && info.hitObject != null && info.hitObject.getComponent(Ground.class) != null) ||
                (info2.hit && info2.hitObject != null && info2.hitObject.getComponent(Ground.class) != null);

        if (!isInvincible()) {
            if (info.hit && info.hitObject != null && info.hitObject.getComponent(GoombaAI.class) != null) {
                if (info.hitObject.getComponent(GoombaAI.class).isAlive()) {
                    info.hitObject.getComponent(GoombaAI.class).stomp();
                    this.enemyBounce();
                }
            } else if (info2.hit && info2.hitObject != null && info2.hitObject.getComponent(GoombaAI.class) != null) {
                if (info2.hitObject.getComponent(GoombaAI.class).isAlive()) {
                    info2.hitObject.getComponent(GoombaAI.class).stomp();
                    this.enemyBounce();
                }
            }
        }

        //DebugDraw.addLine2D(raycastBegin, raycastEnd, new Vector3f(1, 0, 0));
        //DebugDraw.addLine2D(raycast2Begin, raycast2End);
    }

    public void enemyBounce() {
        this.enemyBounce = 8;
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

    public void die() {
        this.stateMachine.trigger("die");
        if (this.playerState == PlayerState.Small) {
            this.velocity.set(0, 0);
            this.acceleration.set(0, 0);
            this.rb.setVelocity(new Vector2f());
            this.isDead = true;
            this.rb.setIsSensor();
            AssetPool.getSound("assets/sounds/mario_die.ogg").play();
            deadMaxHeight = this.gameObject.transform.position.y + 0.3f;
            this.rb.setBodyType(BodyType.Static);
            if (gameObject.transform.position.y > 0) {
                deadMinHeight = -0.25f;
            }
        } else if (this.playerState == PlayerState.Big) {
            this.playerState = PlayerState.Small;
            gameObject.transform.scale.y = 0.25f;
            PillboxCollider pb = gameObject.getComponent(PillboxCollider.class);
            if (pb != null) {
                jumpBoost /= bigJumpBoostFactor;
                walkSpeed /= bigJumpBoostFactor;
                pb.setHeight(0.31f);
            }
            hurtInvincibilityTimeLeft = hurtInvincibilityTime;
            AssetPool.getSound("assets/sounds/pipe.ogg").play();
        } else if (this.playerState == PlayerState.Fire) {
            this.playerState = PlayerState.Big;
            hurtInvincibilityTimeLeft = hurtInvincibilityTime;
            AssetPool.getSound("assets/sounds/pipe.ogg").play();
        }
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (isDead) return;

        if (collidingObject.getComponent(Ground.class) != null) {
            if (Math.abs(contactNormal.x) > 0.8f) {
                this.velocity.x = 0;
            } else if (contactNormal.y > 0.8f) {
                this.velocity.y = 0;
                this.acceleration.y = 0;
                this.jumpTime = 0;
            }
        }
    }

    @Override
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (collidingObject.getComponent(GoombaAI.class) != null && hurtInvincibilityTimeLeft > 0) {
            contact.setEnabled(false);
        }
    }

    public boolean isDead() {
        return this.isDead;
    }

    public boolean isInvincible() {
        return this.playerState == PlayerState.Invincible || this.hurtInvincibilityTimeLeft > 0;
    }

    public boolean isHurtInvincible() {
        return this.hurtInvincibilityTimeLeft > 0;
    }

    public void setPosition(Vector2f newPos) {
        this.gameObject.transform.position.set(newPos);
        this.rb.setPosition(newPos);
    }

    public void playWinAnimation(GameObject flagpole) {
        if (!playWinAnimation) {
            this.playWinAnimation = true;
            this.velocity.set(0, 0);
            this.acceleration.set(0, 0);
            this.rb.setVelocity(new Vector2f());
            this.rb.setIsSensor();
            this.rb.setBodyType(BodyType.Static);
            this.gameObject.transform.position.x = flagpole.transform.position.x;
            AssetPool.getSound("assets/sounds/flagpole.ogg").play();
        }
    }

    public boolean hasWon() {
        return this.playWinAnimation;
    }
}