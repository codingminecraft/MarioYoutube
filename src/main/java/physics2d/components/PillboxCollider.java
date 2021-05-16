package physics2d.components;

import components.Component;
import org.joml.Vector2f;

public class PillboxCollider extends Component {
    private transient CircleCollider topCircle = new CircleCollider();
    private transient CircleCollider bottomCircle = new CircleCollider();
    private transient Box2DCollider box = new Box2DCollider();

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
        recalculateColliders();
        topCircle.editorUpdate(dt);
        bottomCircle.editorUpdate(dt);
        box.editorUpdate(dt);
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
        box.setHalfSize(new Vector2f(width / 2.0f, boxHeight / 2.0f));
        box.setOffset(offset);
    }
}
