package infrastructure.adapter.driving.listener;

import domain.port.outbound.model.Event;
import domain.port.outbound.model.EventType;
import infrastructure.adapter.driven.handler.EventHandler;
import infrastructure.adapter.driving.processor.EventProcessor;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class EventListener {

    private final Map<EventType, EventProcessor> eventProcessors;
    private final EventHandler eventHandler;
    private volatile boolean isRunning = true;

    public void fetch() {
        while (isRunning) {
            if (!eventHandler.isEmpty()) {
                var event = eventHandler.pull();
                processEvent(event);
            }
        }
    }

    private void processEvent(Event event) {
        var eventProcessor = eventProcessors.get(event.getEventType());
        eventProcessor.process(event);
    }

    public void stop() {
        isRunning = false;
    }
}
