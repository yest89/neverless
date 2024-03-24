package domain.usecase.util;

import domain.model.Account;
import domain.model.AccountOperationId;
import domain.model.Amount;
import domain.exception.DepositOperationRollback;
import domain.exception.ReserveOperationRollback;

public interface DepositAccountRetry {
    void retry(AccountDepositTaskFunction<AccountOperationId, Account, Amount, Boolean> lambda,
               AccountOperationId accountOperationId,
               Account account,
               Amount amount) throws ReserveOperationRollback, DepositOperationRollback;
}
