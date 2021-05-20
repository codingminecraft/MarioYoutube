package physics2d.components;

import components.Component;
import jade.Window;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;
import renderer.DebugDraw;

public class Box2DCollider extends Component {
    private Vector2f halfSize = new Vector2f(1);
    private Vector2f offset = new Vector2f();
    private transient boolean resetFixtureNextFrame = false;
    private boolean isSensor = false;

    public Vector2f getOffset() {
        return this.offset;
    }

    public Vector2f getHalfSize() {
        return halfSize;
    }

    public void setHalfSize(Vector2f halfSize) {
        this.halfSize = halfSize;
        resetFixture();
    }

    public void setOffset(Vector2f offset) {
        this.offset.set(offset);
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
                Window.getPhysics().resetBoxCollider(rb, this);
            }
        }
    }

    @Override
    public void editorUpdate(float dt) {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugDraw.addBox2D(center, this.halfSize, this.gameObject.transform.rotation);

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

    public void setSensor(boolean isSensor) {
        this.isSensor = isSensor;
    }

    public boolean getIsSensor() {
        return this.isSensor;
    }
}
