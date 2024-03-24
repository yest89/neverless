package infrastructure.util;

import domain.model.AccountId;
import domain.model.AccountOperationId;
import domain.model.Amount;
import domain.exception.ReserveOperationRollback;
import domain.usecase.util.AccountWithdrawTaskFunction;
import domain.usecase.util.WithdrawAccountRetry;

import java.time.Duration;

public class AccountWithdrawRetryWrapper implements WithdrawAccountRetry {
    private static final int ATTEMPT_COUNTER = 5;
    private static final long DEPOSIT_WAITING_TIME = Duration.ofSeconds(2).toSeconds();

    @Override
    public void retry(AccountWithdrawTaskFunction<AccountOperationId, AccountId, Amount, Boolean> lambda,
                      AccountOperationId accountOperationId,
                      AccountId accountId,
                      Amount amount) throws ReserveOperationRollback {
        Boolean result;
        for (int i = 0; i < ATTEMPT_COUNTER; i++) {
            try {
                result = lambda.apply(accountOperationId, accountId, amount);
            } catch (ReserveOperationRollback ex) {
                continue;
            }

            if (result) {
                return;
            }
            try {
                Thread.sleep(DEPOSIT_WAITING_TIME);
            } catch (InterruptedException e) {
                //LOG
            }
        }
        throw new ReserveOperationRollback();
    }
}
