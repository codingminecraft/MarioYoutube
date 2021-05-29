package observers;

import com.sun.jmx.remote.internal.ArrayQueue;
import jade.GameObject;
import observers.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class EventSystem {
    private static class EventDTO {
        GameObject obj;
        Event event;

        public EventDTO(GameObject obj, Event event) {
            this.obj = obj;
            this.event = event;
        }
    }

    private static List<Observer> observers = new ArrayList<>();
    private static List<EventDTO> eventQueue = new ArrayList<>();

    public static void addObserver(Observer observer) {
        observers.add(observer);
    }

    public static void notify(GameObject obj, Event event) {
        eventQueue.add(new EventDTO(obj, event));
    }

    public static void notifyQueuedEvents() {
        for (EventDTO edt : eventQueue) {
            for (Observer observer : observers) {
                observer.onNotify(edt.obj, edt.event);
            }
        }
        eventQueue.clear();
    }
}
