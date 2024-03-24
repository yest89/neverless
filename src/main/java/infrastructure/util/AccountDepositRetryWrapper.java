package infrastructure.util;

import domain.model.Account;
import domain.model.AccountOperationId;
import domain.model.Amount;
import domain.exception.DepositOperationRollback;
import domain.exception.ReserveOperationRollback;
import domain.usecase.util.AccountDepositTaskFunction;
import domain.usecase.util.DepositAccountRetry;

import java.time.Duration;

public class AccountDepositRetryWrapper implements DepositAccountRetry {
    private static final int ATTEMPT_COUNTER = 5;
    private static final long DEPOSIT_WAITING_TIME = Duration.ofSeconds(2).toSeconds();

    @Override
    public void retry(AccountDepositTaskFunction<AccountOperationId, Account, Amount, Boolean> lambda,
                      AccountOperationId accountOperationId,
                      Account account,
                      Amount amount) throws ReserveOperationRollback, DepositOperationRollback {
        Boolean result;
        for (int i = 0; i < ATTEMPT_COUNTER; i++) {
            try {
                result = lambda.apply(accountOperationId, account, amount);
            } catch (ReserveOperationRollback e) {
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
