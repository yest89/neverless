package domain.port.outbound.model;

import domain.model.AccountId;
import domain.model.AccountOperationId;
import domain.model.Amount;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WithdrawAccountEvent implements Event {

    private AccountOperationId accountOperationId;
    private AccountId accountIdA;
    private Amount amount;
    private EventType eventType;

    @Override
    public AccountId getAccountIdB() {
        throw new IllegalArgumentException();
    }
}
