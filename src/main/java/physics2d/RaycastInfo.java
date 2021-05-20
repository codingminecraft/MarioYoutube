package physics2d;

import jade.GameObject;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;

public class RaycastInfo implements RayCastCallback {
    public Fixture fixture;
    public Vector2f point;
    public Vector2f normal;
    public float fraction;
    public boolean hit;
    public GameObject hitObject;
    private GameObject requestingObject;

    public RaycastInfo(GameObject requestingObject) {
        fixture = null;
        point = new Vector2f();
        normal = new Vector2f();
        fraction = 0.0f;
        hit = false;
        hitObject = null;
        this.requestingObject = requestingObject;
    }

    public float reportFixture(Fixture var1, Vec2 var2, Vec2 var3, float var4) {
        if (var1.m_userData == requestingObject) {
            return 1;
        }
        this.fixture = var1;
        this.point = new Vector2f(var2.x, var2.y);
        this.normal = new Vector2f(var3.x, var3.y);
        this.fraction = var4;
        this.hit = fraction != 0;
        this.hitObject = (GameObject)var1.m_userData;

        return var4;
    }
}
