package physics2d;

import components.Component;
import jade.GameObject;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;
import physics2d.components.Rigidbody2D;

public class JadeContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        GameObject objA = (GameObject)contact.getFixtureA().getUserData();
        GameObject objB = (GameObject)contact.getFixtureB().getUserData();
        for (Component c : objA.getAllComponents()) {
            c.beginCollision(objB, contact);
        }

        for (Component c : objB.getAllComponents()) {
            c.beginCollision(objA, contact);
        }
    }

    @Override
    public void endContact(Contact contact) {
        GameObject objA = (GameObject)contact.m_fixtureA.m_userData;
        GameObject objB = (GameObject)contact.m_fixtureB.m_userData;
        for (Component c : objA.getAllComponents()) {
            c.endCollision(objB, contact);
        }

        for (Component c : objB.getAllComponents()) {
            c.endCollision(objA, contact);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
        GameObject objA = (GameObject)contact.getFixtureA().getUserData();
        GameObject objB = (GameObject)contact.getFixtureB().getUserData();
        for (Component c : objA.getAllComponents()) {
            c.preSolve(objB, contact);
        }

        for (Component c : objB.getAllComponents()) {
            c.preSolve(objA, contact);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
        GameObject objA = (GameObject)contact.getFixtureA().getUserData();
        GameObject objB = (GameObject)contact.getFixtureB().getUserData();
        for (Component c : objA.getAllComponents()) {
            c.postSolve(objB, contact);
        }

        for (Component c : objB.getAllComponents()) {
            c.postSolve(objA, contact);
        }
    }
}
