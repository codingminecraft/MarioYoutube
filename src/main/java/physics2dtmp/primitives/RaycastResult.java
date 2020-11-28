package physics2dtmp.primitives;

import org.joml.Vector2f;

public class RaycastResult {
    private Vector2f point;
    private Vector2f normal;
    private float t;
    private boolean hit;

    public RaycastResult() {
        this.point = new Vector2f();
        this.normal = new Vector2f();
        this.t = -1;
        this.hit = false;
    }

    public void init(Vector2f point, Vector2f normal, float t, boolean hit) {
        this.point.set(point);
        this.normal.set(normal);
        this.t = t;
        this.hit = hit;
    }

    public static void reset(RaycastResult result) {
        if (result != null) {
            result.point.zero();
            result.normal.set(0, 0);
            result.t = -1;
            result.hit = false;
        }
    }
}
