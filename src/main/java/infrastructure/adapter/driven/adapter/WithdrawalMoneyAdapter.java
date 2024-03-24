package infrastructure.adapter.driven.adapter;

import domain.model.AccountId;
import domain.model.AccountOperationId;
import domain.model.WithdrawTaskStatus;
import domain.port.outbound.WithdrawalMoneyOutbound;
import infrastructure.adapter.driven.exception.WithdrawalStatusException;
import infrastructure.thirdparty.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WithdrawalMoneyAdapter implements WithdrawalMoneyOutbound {
    private final WithdrawalService withdrawalService;

    @Override
    public void withdraw(AccountOperationId accountOperationId, AccountId accountId, domain.model.Amount withdrawAmount) {
        WithdrawalId withdrawalId = new WithdrawalId(accountOperationId.operationId());
        withdrawalService.requestWithdrawal(
                withdrawalId,
                new Address(accountId.accountId()),
                new Amount(withdrawAmount.amount())
        );
    }

    @Override
    public WithdrawTaskStatus getWithdrawState(AccountOperationId accountOperationId) throws WithdrawalStatusException {
        try {
            WithdrawalId withdrawalId = new WithdrawalId(accountOperationId.operationId());
            WithdrawalState requestState = withdrawalService.getRequestState(withdrawalId);
            return WithdrawTaskStatus.valueOf(requestState.name());
        } catch (Throwable ex) {
            throw new WithdrawalStatusException();
        }
    }
}
