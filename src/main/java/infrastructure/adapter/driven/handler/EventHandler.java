package infrastructure.adapter.driven.handler;

import domain.port.outbound.model.Event;

import java.util.ArrayDeque;
import java.util.Queue;


public class EventHandler {

    private final Queue<Event> events = new ArrayDeque<>();

    public void register(Event event) {
        events.add(event);
    }

    public Event pull() {
        return events.poll();
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }
}
