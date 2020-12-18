package physics2d.components;

import components.Component;
import org.joml.Vector2f;

public abstract class Collider extends Component {
    protected Vector2f offset = new Vector2f();

    public Vector2f getOffset() {
        return this.offset;
    }
}
