package infrastructure.util;

import domain.model.AccountOperationId;
import domain.model.WithdrawTaskStatus;
import domain.usecase.util.WithdrawalStateTaskFunction;
import domain.usecase.util.WithdrawalStatusAccountRetry;
import infrastructure.adapter.driven.exception.WithdrawalStatusException;

import java.time.Duration;

public class WithdrawalStatusAccountRetryWrapper implements WithdrawalStatusAccountRetry {
    private static final int ATTEMPT_COUNTER = 5;
    private static final long WITHDRAW_STATE_WAITING_TIME = Duration.ofSeconds(12).toSeconds();

    @Override
    public WithdrawTaskStatus retry(WithdrawalStateTaskFunction<AccountOperationId, WithdrawTaskStatus> lambda, AccountOperationId accountOperationId) {
        WithdrawTaskStatus withdrawTaskStatus = WithdrawTaskStatus.PROCESSING;
        waitingWithdrawStatusOperation();
        for (int i = 0; i < ATTEMPT_COUNTER; i++) {
            try {
                withdrawTaskStatus = lambda.apply(accountOperationId);
                if (!WithdrawTaskStatus.PROCESSING.equals(withdrawTaskStatus)) {
                    return withdrawTaskStatus;
                }
            } catch (WithdrawalStatusException e) {
                waitingWithdrawStatusOperation();
            }
            waitingWithdrawStatusOperation();
        }
        return withdrawTaskStatus;
    }

    private static void waitingWithdrawStatusOperation() {
        try {
            Thread.sleep(WITHDRAW_STATE_WAITING_TIME);
        } catch (InterruptedException e) {
            //LOG
        }
    }
}
