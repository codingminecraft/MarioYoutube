package physics2d.components;

import components.Component;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;
import physics2d.enums.BodyType;

public class Rigidbody2D extends Component {
    private Vector2f velocity = new Vector2f();
    private float angularDamping = 0.8f;
    private float linearDamping = 0.9f;
    private float mass = 0;
    private BodyType bodyType = BodyType.Dynamic;
    private float friction = 0.1f;

    private boolean fixedRotation = false;
    private boolean continuousCollision = true;

    private transient Body rawBody = null;

    @Override
    public void update(float dt) {
        if (rawBody != null) {
            if (this.bodyType == BodyType.Dynamic) {
                this.gameObject.transform.position.set(
                        rawBody.getPosition().x, rawBody.getPosition().y
                );
                this.gameObject.transform.rotation = (float) Math.toDegrees(rawBody.getAngle());
                Vec2 vel = rawBody.getLinearVelocity();
                this.velocity = new Vector2f(vel.x, vel.y);
            } else {
                this.rawBody.setTransform(
                        new Vec2(gameObject.transform.position.x, gameObject.transform.position.y),
                        0.0f);
            }
        }
    }

    public void addVelocity(Vector2f velocity) {
        rawBody.applyForceToCenter(new Vec2(velocity.x, velocity.y));
    }

    public void addImpulse(Vector2f impulse) {
        rawBody.applyLinearImpulse(new Vec2(impulse.x, impulse.y), rawBody.getWorldCenter());
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public void setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public boolean isContinuousCollision() {
        return continuousCollision;
    }

    public void setContinuousCollision(boolean continuousCollision) {
        this.continuousCollision = continuousCollision;
    }

    public Body getRawBody() {
        return rawBody;
    }

    public void setRawBody(Body rawBody) {
        this.rawBody = rawBody;
    }

    public float getFriction() { return this.friction; }

    public void setFriction(float newVal) { this.friction = newVal; }
}
