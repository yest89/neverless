package domain.usecase.util;

import domain.model.AccountId;
import domain.model.AccountOperationId;
import domain.model.Amount;
import domain.exception.ReserveOperationRollback;

public interface WithdrawAccountRetry {
    void retry(AccountWithdrawTaskFunction<AccountOperationId, AccountId, Amount, Boolean> lambda,
               AccountOperationId accountOperationId,
               AccountId accountId,
               Amount amount) throws ReserveOperationRollback;
}
