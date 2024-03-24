package domain.port.inbound;

import domain.model.Account;
import domain.model.AccountOperationId;
import domain.model.Amount;

public interface ReserveAccountInbound {
    void reserve(Account account, Amount reservedAmount, AccountOperationId accountOperationId);
}
