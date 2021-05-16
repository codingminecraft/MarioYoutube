package physics2d;

import jade.GameObject;
import jade.Transform;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.PillboxCollider;
import physics2d.components.Rigidbody2D;

public class Physics2D {
    private Vec2 gravity = new Vec2(0, -10.0f);
    private World world = new World(gravity);

    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f / 60.0f;
    private int velocityIterations = 8;
    private int positionIterations = 3;

    public Physics2D() {
        world.setContactListener(new JadeContactListener());
    }

    public void add(GameObject go) {
        Rigidbody2D rb = go.getComponent(Rigidbody2D.class);
        if (rb != null && rb.getRawBody() == null) {
            Transform transform = go.transform;

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float)Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rb.getAngularDamping();
            bodyDef.linearDamping = rb.getLinearDamping();
            bodyDef.fixedRotation = rb.isFixedRotation();
            bodyDef.bullet = rb.isContinuousCollision();

            switch (rb.getBodyType()) {
                case Kinematic: bodyDef.type = BodyType.KINEMATIC; break;
                case Static: bodyDef.type = BodyType.STATIC; break;
                case Dynamic: bodyDef.type = BodyType.DYNAMIC; break;
            }

            Body body = this.world.createBody(bodyDef);
            body.m_mass = rb.getMass();
            body.setUserData(go);
            CircleCollider circleCollider;
            Box2DCollider boxCollider;
            PillboxCollider pillboxCollider;

            if ((circleCollider = go.getComponent(CircleCollider.class)) != null) {
                CircleShape shape = new CircleShape();
                shape.setRadius(circleCollider.getRadius());
                shape.m_p.set(circleCollider.getOffset().x, circleCollider.getOffset().y);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = 1.0f;
                fixtureDef.friction = rb.getFriction();
                fixtureDef.userData = go;
                body.createFixture(fixtureDef);
            }

            if ((boxCollider = go.getComponent(Box2DCollider.class)) != null) {
                PolygonShape shape = new PolygonShape();
                Vector2f halfSize = new Vector2f(boxCollider.getHalfSize()).mul(0.5f);
                Vector2f offset = boxCollider.getOffset();
                shape.setAsBox(halfSize.x, halfSize.y, new Vec2(offset.x, offset.y), 0);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = 1.0f;
                fixtureDef.friction = rb.getFriction();
                fixtureDef.userData = go;
                body.createFixture(fixtureDef);
            }

            if ((pillboxCollider = go.getComponent(PillboxCollider.class)) != null) {
                PolygonShape boxShape = new PolygonShape();
                Vector2f halfSize = new Vector2f(pillboxCollider.getBox().getHalfSize()).mul(0.5f);
                Vector2f offset = pillboxCollider.getBox().getOffset();
                boxShape.setAsBox(halfSize.x, halfSize.y, new Vec2(offset.x, offset.y), 0);
                FixtureDef boxFixtureDef = new FixtureDef();
                boxFixtureDef.shape = boxShape;
                boxFixtureDef.density = 1.0f;
                boxFixtureDef.friction = rb.getFriction();
                boxFixtureDef.userData = go;
                body.createFixture(boxFixtureDef);

                CircleShape topCircleShape = new CircleShape();
                topCircleShape.setRadius(pillboxCollider.getTopCircle().getRadius());
                topCircleShape.m_p.set(pillboxCollider.getTopCircle().getOffset().x, pillboxCollider.getTopCircle().getOffset().y);
                FixtureDef topCircleFixtureDef = new FixtureDef();
                topCircleFixtureDef.shape = topCircleShape;
                topCircleFixtureDef.density = 1.0f;
                topCircleFixtureDef.friction = rb.getFriction();
                topCircleFixtureDef.userData = go;
                body.createFixture(topCircleFixtureDef);

                CircleShape bottomCircleShape = new CircleShape();
                bottomCircleShape.setRadius(pillboxCollider.getBottomCircle().getRadius());
                topCircleShape.m_p.set(pillboxCollider.getBottomCircle().getOffset().x, pillboxCollider.getBottomCircle().getOffset().y);
                FixtureDef bottomCircleFixtureDef = new FixtureDef();
                bottomCircleFixtureDef.shape = topCircleShape;
                bottomCircleFixtureDef.density = 1.0f;
                bottomCircleFixtureDef.friction = rb.getFriction();
                bottomCircleFixtureDef.userData = go;
                body.createFixture(bottomCircleFixtureDef);
            }
            rb.setRawBody(body);
        }
    }

    public void destroyGameObject(GameObject go) {
        Rigidbody2D rb = go.getComponent(Rigidbody2D.class);
        if (rb != null) {
            if (rb.getRawBody() != null) {
                world.destroyBody(rb.getRawBody());
                rb.setRawBody(null);
            }
        }
    }

    public void update(float dt) {
        physicsTime += dt;
        if (physicsTime >= 0.0f) {
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }
}
