package domain.port.inbound;

import domain.model.Account;
import domain.model.AccountId;
import domain.model.AccountOperationId;
import domain.model.Amount;

public interface DepositAccountInbound {
    void deposit(Account account, AccountId accountIdB, Amount depositAmount, AccountOperationId accountOperationId);
}
