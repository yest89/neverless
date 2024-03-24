package domain.port.outbound.model;

import domain.model.AccountId;
import domain.model.AccountOperationId;
import domain.model.Amount;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DepositAccountEvent implements Event {

    private AccountOperationId accountOperationId;
    private AccountId accountIdA;
    private AccountId accountIdB;
    private Amount amount;
    private EventType eventType;
}
