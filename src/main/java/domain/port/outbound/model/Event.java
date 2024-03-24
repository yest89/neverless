package domain.port.outbound.model;

import domain.model.AccountId;
import domain.model.AccountOperationId;
import domain.model.Amount;

public interface Event {
    AccountId getAccountIdA();
    AccountId getAccountIdB();

    Amount getAmount();

    AccountOperationId getAccountOperationId();

    EventType getEventType();
}
