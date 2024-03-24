package infrastructure.adapter.driving.processor;

import domain.model.OperationStatus;
import domain.port.inbound.ReserveAccountInbound;
import domain.port.outbound.model.Event;
import domain.port.outbound.GetAccountOutbound;
import domain.port.outbound.SaveOperationStatusOutbound;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReserveEventProcessor implements EventProcessor {

    private final GetAccountOutbound getAccountOutbound;
    private final ReserveAccountInbound reserveAccountInbound;
    private final SaveOperationStatusOutbound saveOperationStatusOutbound;

    @Override
    public void process(Event event) {
        var acc = getAccountOutbound.get(event.getAccountIdA());
        if (acc.isEmpty()) {
            saveOperationStatusOutbound.saveStatus(event.getAccountOperationId(), OperationStatus.FAILED);
            return;
        }
        reserveAccountInbound.reserve(acc.get(), event.getAmount(), event.getAccountOperationId());
    }
}
