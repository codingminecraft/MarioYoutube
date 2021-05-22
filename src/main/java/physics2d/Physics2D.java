package physics2d;

import jade.GameObject;
import jade.Transform;
import org.jbox2d.collision.shapes.CircleShape;
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
            bodyDef.gravityScale = rb.gravityScale;
            bodyDef.angularVelocity = rb.angularVelocity;

            switch (rb.getBodyType()) {
                case Kinematic: bodyDef.type = BodyType.KINEMATIC; break;
                case Static: bodyDef.type = BodyType.STATIC; break;
                case Dynamic: bodyDef.type = BodyType.DYNAMIC; break;
            }

            Body body = this.world.createBody(bodyDef);
            body.m_mass = rb.getMass();
            body.setUserData(go);
            rb.setRawBody(body);

            CircleCollider circleCollider;
            Box2DCollider boxCollider;
            PillboxCollider pillboxCollider;

            if ((circleCollider = go.getComponent(CircleCollider.class)) != null) {
                addCircleCollider(rb, circleCollider);
            }

            if ((boxCollider = go.getComponent(Box2DCollider.class)) != null) {
                addBox2DCollider(rb, boxCollider);
            }

            if ((pillboxCollider = go.getComponent(PillboxCollider.class)) != null) {
                addPillboxCollider(rb, pillboxCollider);
            }
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

    public void resetCircleCollider(Rigidbody2D rb, CircleCollider circleCollider) {
        Body body = rb.getRawBody();
        if (body == null) return;

        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addCircleCollider(rb, circleCollider);
        body.resetMassData();
    }

    public void addCircleCollider(Rigidbody2D rb, CircleCollider circleCollider) {
        Body body = rb.getRawBody();
        if (body == null) return;

        CircleShape shape = new CircleShape();
        shape.setRadius(circleCollider.getRadius());
        shape.m_p.set(circleCollider.getOffset().x, circleCollider.getOffset().y);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = rb.getFriction();
        fixtureDef.userData = rb.gameObject;
        body.createFixture(fixtureDef);
    }

    public void resetBoxCollider(Rigidbody2D rb, Box2DCollider boxCollider) {
        Body body = rb.getRawBody();
        if (body == null) return;

        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addBox2DCollider(rb, boxCollider);
        body.resetMassData();
    }

    public void addBox2DCollider(Rigidbody2D rb, Box2DCollider boxCollider) {
        Body body = rb.getRawBody();
        assert body != null : "Raw body must not be null";

        PolygonShape shape = new PolygonShape();
        Vector2f halfSize = new Vector2f(boxCollider.getHalfSize()).mul(0.5f);
        Vector2f offset = boxCollider.getOffset();
        shape.setAsBox(halfSize.x, halfSize.y, new Vec2(offset.x, offset.y), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = rb.getFriction();
        fixtureDef.userData = boxCollider.gameObject;
        body.createFixture(fixtureDef);
    }

    public void resetPillboxCollider(Rigidbody2D rb, PillboxCollider pillboxCollider) {
        Body body = rb.getRawBody();
        if (body == null) return;

        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addPillboxCollider(rb, pillboxCollider);
        body.resetMassData();
    }

    public void addPillboxCollider(Rigidbody2D rb, PillboxCollider pillboxCollider) {
        Body body = rb.getRawBody();
        assert body != null : "Raw body must not be null";

        PolygonShape boxShape = new PolygonShape();
        Vector2f halfSize = new Vector2f(pillboxCollider.getBox().getHalfSize()).mul(0.5f);
        Vector2f offset = pillboxCollider.getBox().getOffset();
        boxShape.setAsBox(halfSize.x, halfSize.y, new Vec2(offset.x, offset.y), 0);
        FixtureDef boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = boxShape;
        boxFixtureDef.density = 1.0f;
        boxFixtureDef.friction = rb.getFriction();
        boxFixtureDef.userData = rb.gameObject;
        body.createFixture(boxFixtureDef);

        CircleShape topCircleShape = new CircleShape();
        topCircleShape.setRadius(pillboxCollider.getTopCircle().getRadius());
        topCircleShape.m_p.set(pillboxCollider.getTopCircle().getOffset().x, pillboxCollider.getTopCircle().getOffset().y);
        FixtureDef topCircleFixtureDef = new FixtureDef();
        topCircleFixtureDef.shape = topCircleShape;
        topCircleFixtureDef.density = 1.0f;
        topCircleFixtureDef.friction = rb.getFriction();
        topCircleFixtureDef.userData = rb.gameObject;
        body.createFixture(topCircleFixtureDef);

        CircleShape bottomCircleShape = new CircleShape();
        bottomCircleShape.setRadius(pillboxCollider.getBottomCircle().getRadius());
        topCircleShape.m_p.set(pillboxCollider.getBottomCircle().getOffset().x, pillboxCollider.getBottomCircle().getOffset().y);
        FixtureDef bottomCircleFixtureDef = new FixtureDef();
        bottomCircleFixtureDef.shape = topCircleShape;
        bottomCircleFixtureDef.density = 1.0f;
        bottomCircleFixtureDef.friction = rb.getFriction();
        bottomCircleFixtureDef.userData = rb.gameObject;
        body.createFixture(bottomCircleFixtureDef);
    }

    public RaycastInfo raycast(GameObject requestingObject, Vector2f point1, Vector2f point2) {
        RaycastInfo callback = new RaycastInfo(requestingObject);
        world.raycast(callback, new Vec2(point1.x, point1.y), new Vec2(point2.x, point2.y));
        return callback;
    }

    public boolean isLocked() {
        return this.world.isLocked();
    }

    private int fixtureListSize(Body body) {
        int size = 0;
        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            size++;
            fixture = fixture.m_next;
        }
        return size;
    }

    public Vector2f getGravity() {
        return new Vector2f(this.world.getGravity().x, this.world.getGravity().y);
    }
}
