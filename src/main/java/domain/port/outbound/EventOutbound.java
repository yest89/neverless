package domain.port.outbound;

import domain.port.outbound.model.Event;

public interface EventOutbound {
    void publish(Event event);
}
