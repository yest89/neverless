package infrastructure.adapter.driving.processor;

import domain.model.OperationStatus;
import domain.port.inbound.DepositAccountInbound;
import domain.port.outbound.GetAccountOutbound;
import domain.port.outbound.SaveOperationStatusOutbound;
import domain.port.outbound.model.Event;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DepositEventProcessor implements EventProcessor {

    private final GetAccountOutbound getAccountOutbound;
    private final SaveOperationStatusOutbound saveOperationStatusOutbound;
    private final DepositAccountInbound depositAccountUseCase;

    @Override
    public void process(Event event) {
        var acc = getAccountOutbound.get(event.getAccountIdB());
        if (acc.isEmpty()) {
            saveOperationStatusOutbound.saveStatus(event.getAccountOperationId(), OperationStatus.FAILED);
            return;
        }
        depositAccountUseCase.deposit(acc.get(), event.getAccountIdA(), event.getAmount(), event.getAccountOperationId());
    }
}
