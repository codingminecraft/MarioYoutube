package physics2d.components;

import components.Component;
import jade.Window;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;

public class PillboxCollider extends Component {
    private transient CircleCollider topCircle = new CircleCollider();
    private transient CircleCollider bottomCircle = new CircleCollider();
    private transient Box2DCollider box = new Box2DCollider();
    private transient boolean resetFixtureNextFrame = false;

    public float width = 0.1f;
    public float height = 0.2f;
    public Vector2f offset = new Vector2f();

    @Override
    public void start() {
        this.topCircle.gameObject = this.gameObject;
        this.bottomCircle.gameObject = this.gameObject;
        this.box.gameObject = this.gameObject;
        recalculateColliders();
    }

    @Override
    public void editorUpdate(float dt) {
        topCircle.editorUpdate(dt);
        bottomCircle.editorUpdate(dt);
        box.editorUpdate(dt);

        if (resetFixtureNextFrame) {
            resetFixture();
        }
    }

    @Override
    public void update(float dt) {
        if (resetFixtureNextFrame) {
            resetFixture();
        }
    }

    public void setWidth(float newVal) {
        this.width = newVal;
        recalculateColliders();

        Rigidbody2D rb = gameObject.getComponent(Rigidbody2D.class);
        if (rb != null) {
            Window.getPhysics().resetPillboxCollider(rb, this);
        }
    }

    public void setHeight(float newVal) {
        this.height = newVal;
        recalculateColliders();
        resetFixture();
    }

    private void resetFixture() {
        if (Window.getPhysics().isLocked()) {
            resetFixtureNextFrame = true;
            return;
        }
        resetFixtureNextFrame = false;

        if (gameObject != null) {
            Rigidbody2D rb = gameObject.getComponent(Rigidbody2D.class);
            if (rb != null) {
                Window.getPhysics().resetPillboxCollider(rb, this);
            }
        }
    }

    public Box2DCollider getBox() {
        return this.box;
    }

    public CircleCollider getTopCircle() {
        return this.topCircle;
    }

    public CircleCollider getBottomCircle() {
        return this.bottomCircle;
    }

    public void recalculateColliders() {
        float circleRadius = width / 4.0f;
        float boxHeight = height - 2 * circleRadius;
        topCircle.setRadius(circleRadius);
        bottomCircle.setRadius(circleRadius);
        topCircle.setOffset(new Vector2f(offset).add(0, boxHeight / 4.0f));
        bottomCircle.setOffset(new Vector2f(offset).sub(0, boxHeight / 4.0f));
        box.setHalfSize(new Vector2f((width / 2.0f) - 0.03f, boxHeight / 2.0f));
        box.setOffset(offset);
    }
}
