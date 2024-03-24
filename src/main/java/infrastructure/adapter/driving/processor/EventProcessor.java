package infrastructure.adapter.driving.processor;

import domain.port.outbound.model.Event;

public interface EventProcessor {

    void process(Event event);
}
