package infrastructure.adapter.driving.processor;

import domain.port.inbound.WithdrawalMoneyInbound;
import domain.port.outbound.model.Event;

public record WithdrawEventProcessor(WithdrawalMoneyInbound withdrawalMoneyInbound) implements EventProcessor {
    @Override
    public void process(Event event) {
        withdrawalMoneyInbound.requestWithdrawal(event.getAccountIdA(), event.getAmount(), event.getAccountOperationId());
    }
}
