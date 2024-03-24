package domain.port.outbound;

import domain.model.AccountId;
import domain.model.AccountOperationId;
import domain.model.WithdrawTaskStatus;
import infrastructure.adapter.driven.exception.WithdrawalStatusException;

public interface WithdrawalMoneyOutbound {

    void withdraw(AccountOperationId accountOperationId, AccountId accountId, domain.model.Amount withdrawAmount);

    WithdrawTaskStatus getWithdrawState(AccountOperationId accountOperationId) throws WithdrawalStatusException;
}
