package physics2dtmp;

import org.joml.Vector2f;
import org.junit.Test;
import physics2dtmp.primitives.Box2D;
import physics2dtmp.primitives.Circle;
import physics2dtmp.rigidbody.IntersectionDetector2D;
import physics2dtmp.rigidbody.Rigidbody2D;
import renderer.Line2D;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class CollisionDetectorTests {
    private final float EPSILON = 0.000001f;

    // ============================================================================================
    // Line Intersection tests
    // ============================================================================================
    @Test
    public void pointOnLine2DShouldReturnTrueTest() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(0, 0);

        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOnLine2DShouldReturnTrueTestTwo() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(12, 4);

        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOnVerticalLineShouldReturnTrue() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(0, 10));
        Vector2f point = new Vector2f(0, 5);

        boolean result = IntersectionDetector2D.pointOnLine(point, line);
        assertTrue(result);
    }

    @Test
    public void pointOnLineShouldReturnTrueTestOne() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(0, 0);

        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOnLineShouldReturnTrueTestTwo() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(6, 2);

        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOnLineShouldReturnFalseTestOne() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(4, 2);

        assertFalse(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOnLineShouldReturnTrueTestThree() {
        Line2D line = new Line2D(new Vector2f(10, 10), new Vector2f(22, 14));
        Vector2f point = new Vector2f(10, 10);

        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOnLineShouldReturnTrueTestFour() {
        Line2D line = new Line2D(new Vector2f(10, 10), new Vector2f(22, 14));
        Vector2f point = new Vector2f(16, 12);

        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOnLineShouldReturnFalseTestTwo() {
        Line2D line = new Line2D(new Vector2f(10, 10), new Vector2f(22, 14));
        Vector2f point = new Vector2f(14, 12);

        assertFalse(IntersectionDetector2D.pointOnLine(point, line));
    }

//    TODO: SHOULD THESE BE IMPLEMENTED
//    @Test
//    public void closestPointToLineTestOne() {
//        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
//        Vector2f point = new Vector2f(6, 2);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, line);
//        Vector2f actualClosestPoint = new Vector2f(6, 2);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToLineTestTwo() {
//        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
//        Vector2f point = new Vector2f(13, 3);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, line);
//        Vector2f actualClosestPoint = new Vector2f(12, 4);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToLineTestThree() {
//        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
//        Vector2f point = new Vector2f(7, 4);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, line);
//        Vector2f actualClosestPoint = new Vector2f(7.5f, 2.5f);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToLineTestFour() {
//        Line2D line = new Line2D(new Vector2f(10, 10), new Vector2f(22, 14));
//        Vector2f point = new Vector2f(16, 12);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, line);
//        Vector2f actualClosestPoint = new Vector2f(16, 12);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToLineTestFive() {
//        Line2D line = new Line2D(new Vector2f(10, 10), new Vector2f(22, 14));
//        Vector2f point = new Vector2f(23, 13);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, line);
//        Vector2f actualClosestPoint = new Vector2f(22, 14);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToLineTestSix() {
//        Line2D line = new Line2D(new Vector2f(10, 10), new Vector2f(22, 14));
//        Vector2f point = new Vector2f(17, 14);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, line);
//        Vector2f actualClosestPoint = new Vector2f(17.5f, 12.5f);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }

    // =========================================================================================================
    // Ray2Dcast IntersectionDetector2D tests
    // =========================================================================================================
    // TODO: SHOULD THESE BE IMPLEMENTED?
//    @Test
//    public void pointOnRayShouldReturnTrueTestOne() {
//        Ray2D ray = new Ray2D(new Vector2f(0), new Vector2f(0.948683f, 0.316228f));
//        Vector2f point = new Vector2f(0, 0);
//
//        assertTrue(IntersectionDetector2D.pointOnRay(point, ray));
//    }
//
//    @Test
//    public void pointOnRayShouldReturnTrueTestTwo() {
//        Ray2D ray = new Ray2D(new Vector2f(0), new Vector2f(0.948683f, 0.316228f));
//        Vector2f point = new Vector2f(6, 2);
//
//        assertTrue(IntersectionDetector2D.pointOnRay(point, ray));
//    }
//
//    @Test
//    public void pointOnRayShouldReturnFalseTestOne() {
//        Ray2D ray = new Ray2D(new Vector2f(0), new Vector2f(0.948683f, 0.316228f));
//        Vector2f point = new Vector2f(-6, -2);
//
//        assertFalse(IntersectionDetector2D.pointOnRay(point, ray));
//    }
//
//    @Test
//    public void pointOnRayShouldReturnFalseTestTwo() {
//        Ray2D ray = new Ray2D(new Vector2f(0), new Vector2f(0.948683f, 0.316228f));
//        Vector2f point = new Vector2f(4, 2);
//
//        assertFalse(IntersectionDetector2D.pointOnRay(point, ray));
//    }
//
//    @Test
//    public void pointOnRayShouldReturnTrueTestThree() {
//        Ray2D ray = new Ray2D(new Vector2f(10, 10), new Vector2f(0.948683f, 0.316228f));
//        Vector2f point = new Vector2f(10, 10);
//
//        assertTrue(IntersectionDetector2D.pointOnRay(point, ray));
//    }
//
//    @Test
//    public void pointOnRayShouldReturnTrueTestFour() {
//        Ray2D ray = new Ray2D(new Vector2f(10, 10), new Vector2f(0.948683f, 0.316228f));
//        Vector2f point = new Vector2f(16, 12);
//
//        assertTrue(IntersectionDetector2D.pointOnRay(point, ray));
//    }
//
//    @Test
//    public void pointOnRayShouldReturnFalseTestThree() {
//        Ray2D ray = new Ray2D(new Vector2f(10, 10), new Vector2f(0.948683f, 0.316228f));
//        Vector2f point = new Vector2f(-6 + 10, -2 + 10);
//
//        assertFalse(IntersectionDetector2D.pointOnRay(point, ray));
//    }
//
//    @Test
//    public void pointOnRayShouldReturnFalseTestFour() {
//        Ray2D ray = new Ray2D(new Vector2f(10, 10), new Vector2f(0.948683f, 0.316228f));
//        Vector2f point = new Vector2f(14, 12);
//
//        assertFalse(IntersectionDetector2D.pointOnRay(point, ray));
//    }
//
//    @Test
//    public void closestPointToRayTestOne() {
//        Ray2D ray = new Ray2D(new Vector2f(0, 0), new Vector2f(0.948683f, 0.316228f));
//        Vector2f point = new Vector2f(-1, -1);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, ray);
//        Vector2f actualClosestPoint = new Vector2f(0, 0);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToRayTestTwo() {
//        Ray2D ray = new Ray2D(new Vector2f(0, 0), new Vector2f((float)(3.0 / Math.sqrt(10f)), (float)(1.0 / Math.sqrt(10f))));
//        Vector2f point = new Vector2f(6, 2);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, ray);
//        Vector2f actualClosestPoint = new Vector2f(6, 2);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint, EPSILON));
//    }
//
//    @Test
//    public void closestPointToRayTestThree() {
//        Ray2D ray = new Ray2D(new Vector2f(0, 0), new Vector2f((float)(3.0 / Math.sqrt(10f)), (float)(1.0 / Math.sqrt(10f))));
//        Vector2f point = new Vector2f(7, 4);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, ray);
//        Vector2f actualClosestPoint = new Vector2f(7.5f, 2.5f);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint, EPSILON));
//    }
//
//    @Test
//    public void closestPointToRayTestFour() {
//        Ray2D ray = new Ray2D(new Vector2f(10, 10), new Vector2f(0.948683f, 0.316228f));
//        Vector2f point = new Vector2f(9, 9);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, ray);
//        Vector2f actualClosestPoint = new Vector2f(10, 10);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToRayTestFive() {
//        Ray2D ray = new Ray2D(new Vector2f(10, 10), new Vector2f((float)(3.0 / Math.sqrt(10f)), (float)(1.0 / Math.sqrt(10f))));
//        Vector2f point = new Vector2f(16, 12);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, ray);
//        Vector2f actualClosestPoint = new Vector2f(16, 12);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint, EPSILON));
//    }
//
//    @Test
//    public void closestPointToRayTestSix() {
//        Ray2D ray = new Ray2D(new Vector2f(10, 10), new Vector2f((float)(3.0 / Math.sqrt(10f)), (float)(1.0 / Math.sqrt(10f))));
//        Vector2f point = new Vector2f(17, 14);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, ray);
//        Vector2f actualClosestPoint = new Vector2f(17.5f, 12.5f);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint, EPSILON));
//    }

    // =========================================================================================================
    // Circle intersection tester tests
    // =========================================================================================================
    @Test
    public void pointInCircleShouldReturnTrueTestOne() {
        Circle circle = new Circle();
        circle.setRadius(5f);
        Rigidbody2D body = new Rigidbody2D();
        circle.setRigidbody(body);

        Vector2f point = new Vector2f(3, -2);

        boolean result = IntersectionDetector2D.pointInCircle(point, circle);
        assertTrue(result);
    }

    @Test
    public void pointInCircleShouldReturnTrueTestTwo() {
        Circle circle = new Circle();
        circle.setRadius(5f);
        Rigidbody2D body = new Rigidbody2D();
        circle.setRigidbody(body);

        Vector2f point = new Vector2f(-4.9f, 0);

        boolean result = IntersectionDetector2D.pointInCircle(point, circle);
        assertTrue(result);
    }

    @Test
    public void pointInCircleShouldReturnFalseTestOne() {
        Circle circle = new Circle();
        circle.setRadius(5f);
        Rigidbody2D body = new Rigidbody2D();
        circle.setRigidbody(body);

        Vector2f point = new Vector2f(-6, -6);

        boolean result = IntersectionDetector2D.pointInCircle(point, circle);
        assertFalse(result);
    }

    @Test
    public void pointInCircleShouldReturnTrueTestFour() {
        Circle circle = new Circle();
        circle.setRadius(5f);
        Rigidbody2D body = new Rigidbody2D();
        body.setTransform(new Vector2f(10));
        circle.setRigidbody(body);

        Vector2f point = new Vector2f(3 + 10, -2 + 10);

        boolean result = IntersectionDetector2D.pointInCircle(point, circle);
        assertTrue(result);
    }

    @Test
    public void pointInCircleShouldReturnTrueTestFive() {
        Circle circle = new Circle();
        circle.setRadius(5f);
        Rigidbody2D body = new Rigidbody2D();
        body.setTransform(new Vector2f(10));
        circle.setRigidbody(body);

        Vector2f point = new Vector2f(-4.9f + 10, 0 + 10);

        boolean result = IntersectionDetector2D.pointInCircle(point, circle);
        assertTrue(result);
    }

    @Test
    public void pointInCircleShouldReturnFalseTestTwo() {
        Circle circle = new Circle();
        circle.setRadius(5f);
        Rigidbody2D body = new Rigidbody2D();
        body.setTransform(new Vector2f(10));
        circle.setRigidbody(body);

        Vector2f point = new Vector2f(-6 + 10, -6 + 10);

        boolean result = IntersectionDetector2D.pointInCircle(point, circle);
        assertFalse(result);
    }

//    TODO: IMPLEMENT THESE
//    @Test
//    public void closestPointToCircleTestOne() {
//        Circle circle = new Circle();
//        circle.setRadius(1f);
//        Rigidbody2D body = new Rigidbody2D();
//        circle.setRigidbody(body);
//
//        Vector2f point = new Vector2f(5, 0);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, circle);
//        Vector2f actualClosestPoint = new Vector2f(1, 0);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToCircleTestTwo() {
//        Circle circle = new Circle();
//        circle.setRadius(1f);
//        Rigidbody2D body = new Rigidbody2D();
//        circle.setRigidbody(body);
//
//        Vector2f point = new Vector2f(2.5f, -2.5f);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, circle);
//        Vector2f actualClosestPoint = new Vector2f(0.5773502f, -0.5773502f);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToCircleTestThree() {
//        Circle circle = new Circle();
//        circle.setRadius(1f);
//        Rigidbody2D body = new Rigidbody2D();
//        body.setTransform(new Vector2f(10));
//        circle.setRigidbody(body);
//
//        Vector2f point = new Vector2f(5 + 10, 0 + 10);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, circle);
//        Vector2f actualClosestPoint = new Vector2f(1 + 10, 0 + 10);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToCircleTestFour() {
//        Circle circle = new Circle();
//        circle.setRadius(1f);
//        Rigidbody2D body = new Rigidbody2D();
//        body.setTransform(new Vector2f(10));
//        circle.setRigidbody(body);
//
//        Vector2f point = new Vector2f(2.5f + 10, -2.5f + 10);
//
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, circle);
//        Vector2f actualClosestPoint = new Vector2f(0.5773502f + 10, -0.5773502f + 10);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }

    // =========================================================================================================
    // Box2D intersection tester tests
    // =========================================================================================================
    @Test
    public void pointInBox2DShouldReturnTrueTestOne() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D body = new Rigidbody2D();
        box.setRigidbody(body);

        Vector2f point = new Vector2f(4, 4.3f);

        assertTrue(IntersectionDetector2D.pointInBox2D(point, box));
    }

    @Test
    public void pointInBox2DShouldReturnTrueTestTwo() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D body = new Rigidbody2D();
        box.setRigidbody(body);

        Vector2f point = new Vector2f(-4.9f, -4.9f);

        assertTrue(IntersectionDetector2D.pointInBox2D(point, box));
    }

    @Test
    public void pointInBox2DShouldReturnFalseTestOne() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D body = new Rigidbody2D();
        box.setRigidbody(body);

        Vector2f point = new Vector2f(0, 5.1f);

        assertFalse(IntersectionDetector2D.pointInBox2D(point, box));
    }

    @Test
    public void pointInBox2DShouldReturnTrueTestThree() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D body = new Rigidbody2D();
        body.setTransform(new Vector2f(10));
        box.setRigidbody(body);

        Vector2f point = new Vector2f(4 + 10, 4.3f + 10);

        assertTrue(IntersectionDetector2D.pointInBox2D(point, box));
    }

    @Test
    public void pointInBox2DShouldReturnTrueTestFour() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D body = new Rigidbody2D();
        body.setTransform(new Vector2f(10));
        box.setRigidbody(body);

        Vector2f point = new Vector2f(-4.9f + 10, -4.9f + 10);

        assertTrue(IntersectionDetector2D.pointInBox2D(point, box));
    }

    @Test
    public void pointInBox2DShouldReturnFalseTestTwo() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D body = new Rigidbody2D();
        body.setTransform(new Vector2f(10));
        box.setRigidbody(body);

        Vector2f point = new Vector2f(0 + 10, 5.1f + 10);

        assertFalse(IntersectionDetector2D.pointInBox2D(point, box));
    }

    @Test
    public void pointInRotatedBox2DShouldReturnTrueTestOne() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D body = new Rigidbody2D();
        body.setTransform(new Vector2f(0), 45);
        box.setRigidbody(body);

        Vector2f point = new Vector2f(-1, -1);

        assertTrue(IntersectionDetector2D.pointInBox2D(point, box));
    }

    @Test
    public void pointInRotatedShouldReturnTrueTestTwo() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D body = new Rigidbody2D();
        body.setTransform(new Vector2f(0), 45);
        box.setRigidbody(body);

        Vector2f point = new Vector2f(-3.43553390593f, 3.43553390593f);

        assertTrue(IntersectionDetector2D.pointInBox2D(point, box));
    }

    @Test
    public void pointInRotatedShouldReturnFalseTestOne() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D body = new Rigidbody2D();
        body.setTransform(new Vector2f(10), 45);
        box.setRigidbody(body);

        Vector2f point = new Vector2f(-3.63553390593f, 3.63553390593f);

        assertFalse(IntersectionDetector2D.pointInBox2D(point, box));
    }

    @Test
    public void pointInRotatedBox2DShouldReturnTrueTestThree() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D body = new Rigidbody2D();
        body.setTransform(new Vector2f(10), 45);
        box.setRigidbody(body);

        Vector2f point = new Vector2f(-1 + 10, -1 + 10);

        assertTrue(IntersectionDetector2D.pointInBox2D(point, box));
    }

    @Test
    public void pointInRotatedShouldReturnTrueTestFour() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D body = new Rigidbody2D();
        body.setTransform(new Vector2f(10), 45);
        box.setRigidbody(body);

        Vector2f point = new Vector2f(-3.43553390593f + 10, 3.43553390593f + 10);

        assertTrue(IntersectionDetector2D.pointInBox2D(point, box));
    }

    @Test
    public void pointInRotatedShouldReturnFalseTestTwo() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D body = new Rigidbody2D();
        body.setTransform(new Vector2f(10), 45);
        box.setRigidbody(body);

        Vector2f point = new Vector2f(-3.63553390593f + 10, 3.63553390593f + 10);

        assertFalse(IntersectionDetector2D.pointInBox2D(point, box));
    }


//    TODO: IMPLEMENT THESE FUNCIONS
//    @Test
//    public void closestPointToBox2DTestOne() {
//        Box2D box = new Box2D();
//        box.setSize(new Vector2f(10));
//        Rigidbody2D body = new Rigidbody2D();
//        box.setRigidbody(body);
//
//        Vector2f point = new Vector2f(0, 10);
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, box);
//        Vector2f actualClosestPoint = new Vector2f(0, 5);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToBox2DTestTwo() {
//        Box2D box = new Box2D();
//        box.setSize(new Vector2f(10));
//        Rigidbody2D body = new Rigidbody2D();
//        box.setRigidbody(body);
//
//        Vector2f point = new Vector2f(-6, -4);
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, box);
//        Vector2f actualClosestPoint = new Vector2f(-5, -4);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToBox2DTestThree() {
//        Box2D box = new Box2D();
//        box.setSize(new Vector2f(10));
//        Rigidbody2D body = new Rigidbody2D();
//        box.setRigidbody(body);
//
//        Vector2f point = new Vector2f(3, -4);
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, box);
//        Vector2f actualClosestPoint = new Vector2f(3, -4);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToBox2DTestFour() {
//        Box2D box = new Box2D();
//        box.setSize(new Vector2f(10));
//        Rigidbody2D body = new Rigidbody2D();
//        body.setTransform(new Vector2f(10));
//        box.setRigidbody(body);
//
//        Vector2f point = new Vector2f(0 + 10, 10 + 10);
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, box);
//        Vector2f actualClosestPoint = new Vector2f(0 + 10, 5 + 10);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToBox2DTestFive() {
//        Box2D box = new Box2D();
//        box.setSize(new Vector2f(10));
//        Rigidbody2D body = new Rigidbody2D();
//        body.setTransform(new Vector2f(10));
//        box.setRigidbody(body);
//
//        Vector2f point = new Vector2f(-6 + 10, -4 + 10);
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, box);
//        Vector2f actualClosestPoint = new Vector2f(-5 + 10, -4 + 10);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToBox2DTestSix() {
//        Box2D box = new Box2D();
//        box.setSize(new Vector2f(10));
//        Rigidbody2D body = new Rigidbody2D();
//        body.setTransform(new Vector2f(10));
//        box.setRigidbody(body);
//
//        Vector2f point = new Vector2f(3 + 10, -4 + 10);
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, box);
//        Vector2f actualClosestPoint = new Vector2f(3 + 10, -4 + 10);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
//    }
//
//    @Test
//    public void closestPointToRotatedBox2DTestOne() {
//        Box2D box = new Box2D();
//        box.setSize(new Vector2f(10));
//        Rigidbody2D body = new Rigidbody2D();
//        body.setTransform(new Vector2f(10), 45);
//        box.setRigidbody(body);
//
//        Vector2f point = new Vector2f(10, 0);
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, box);
//        Vector2f actualClosestPoint = new Vector2f(7.07106781187f, 0);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint, EPSILON));
//    }
//
//    @Test
//    public void closestPointToRotatedBox2DTestTwo() {
//        Box2D box = new Box2D();
//        box.setSize(new Vector2f(10));
//        Rigidbody2D body = new Rigidbody2D();
//        body.setTransform(new Vector2f(10), 45);
//        box.setRigidbody(body);
//
//        Vector2f point = new Vector2f(-5.5355339f, -5.5355339f);
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, box);
//        Vector2f actualClosestPoint = new Vector2f(-3.5355339f, -3.5355339f);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint, EPSILON));
//    }
//
//    @Test
//    public void closestPointToRotatedBox2DTestThree() {
//        Box2D box = new Box2D();
//        box.setSize(new Vector2f(10));
//        Rigidbody2D body = new Rigidbody2D();
//        body.setTransform(new Vector2f(10), 45);
//        box.setRigidbody(body);
//
//        Vector2f point = new Vector2f(0, 7.07106781187f);
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, box);
//        Vector2f actualClosestPoint = new Vector2f(0, 7.07106781187f);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint, EPSILON));
//    }
//
//    @Test
//    public void closestPointToRotatedBox2DTestFour() {
//        Box2D box = new Box2D();
//        box.setSize(new Vector2f(10));
//        Rigidbody2D body = new Rigidbody2D();
//        body.setTransform(new Vector2f(10), 45);
//        box.setRigidbody(body);
//
//        Vector2f point = new Vector2f(10 + 10, 0 + 10);
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, box);
//        Vector2f actualClosestPoint = new Vector2f(7.07106781187f + 10, 0 + 10);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint, EPSILON));
//    }
//
//    @Test
//    public void closestPointToRotatedBox2DTestFive() {
//        Box2D box = new Box2D();
//        box.setSize(new Vector2f(10));
//        Rigidbody2D body = new Rigidbody2D();
//        body.setTransform(new Vector2f(10), 45);
//        box.setRigidbody(body);
//
//        Vector2f point = new Vector2f(-5.5355339f + 10, -5.5355339f + 10);
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, box);
//        Vector2f actualClosestPoint = new Vector2f(-3.5355339f + 10, -3.5355339f + 10);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint, EPSILON));
//    }
//
//    @Test
//    public void closestPointToRotatedBox2DTestSix() {
//        Box2D box = new Box2D();
//        box.setSize(new Vector2f(10));
//        Rigidbody2D body = new Rigidbody2D();
//        body.setTransform(new Vector2f(10), 45);
//        box.setRigidbody(body);
//
//        Vector2f point = new Vector2f(0 + 10, 7.07106781187f + 10);
//        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, box);
//        Vector2f actualClosestPoint = new Vector2f(0 + 10, 7.07106781187f + 10);
//
//        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint, EPSILON));
//    }
}
