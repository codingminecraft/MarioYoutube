package physics2d.components;

import components.Component;
import jade.Window;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;
import renderer.DebugDraw;

public class CircleCollider extends Component {
    private float radius = 1f;
    private Vector2f offset = new Vector2f();
    private transient boolean resetFixtureNextFrame = false;

    public Vector2f getOffset() {
        return this.offset;
    }
    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        resetFixture();
    }

    public void setOffset(Vector2f offset) {
        this.offset.set(offset);
        resetFixture();
    }

    @Override
    public void editorUpdate(float dt) {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugDraw.addCircle(center, this.radius);

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

    private void resetFixture() {
        if (Window.getPhysics().isLocked()) {
            resetFixtureNextFrame = true;
            return;
        }
        resetFixtureNextFrame = false;

        if (gameObject != null) {
            Rigidbody2D rb = gameObject.getComponent(Rigidbody2D.class);
            if (rb != null) {
                Window.getPhysics().resetCircleCollider(rb, this);
            }
        }
    }
}
