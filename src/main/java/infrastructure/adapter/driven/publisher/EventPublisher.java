package infrastructure.adapter.driven.publisher;

import domain.port.outbound.EventOutbound;
import domain.port.outbound.model.Event;
import infrastructure.adapter.driven.handler.EventHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventPublisher implements EventOutbound {

    private final EventHandler eventhandler;

    @Override
    public void publish(Event event) {
        eventhandler.register(event);
    }
}
