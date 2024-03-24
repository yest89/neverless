package domain.usecase.util;

import domain.model.AccountOperationId;
import domain.model.WithdrawTaskStatus;

public interface WithdrawalStatusAccountRetry {
    WithdrawTaskStatus retry(WithdrawalStateTaskFunction<AccountOperationId, WithdrawTaskStatus> lambda,
               AccountOperationId accountOperationId);
}
