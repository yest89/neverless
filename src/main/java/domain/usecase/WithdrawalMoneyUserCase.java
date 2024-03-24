package domain.usecase;

import domain.dto.AccountWithdrawDto;
import domain.exception.ReserveOperationRollback;
import domain.model.*;
import domain.port.inbound.SaveOperationInbound;
import domain.port.inbound.WithdrawalMoneyInbound;
import domain.port.outbound.*;
import domain.port.outbound.model.EventType;
import domain.port.outbound.model.ReserveAccountEvent;
import domain.port.outbound.model.WithdrawAccountEvent;
import domain.usecase.util.AccountWithdrawTaskFunction;
import domain.usecase.util.WithdrawAccountRetry;
import domain.usecase.util.WithdrawalStateTaskFunction;
import domain.usecase.util.WithdrawalStatusAccountRetry;
import infrastructure.adapter.driven.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class WithdrawalMoneyUserCase implements WithdrawalMoneyInbound {
    private final SaveOperationInbound saveOperationInBound;
    private final EventPublisher eventPublisher;
    private final WithdrawAccountRetry withdrawAccountRetry;
    private final SaveOperationStatusOutbound saveOperationStatusOutbound;
    private final GetOperationOutbound getOperationOutbound;
    private final WithdrawalMoneyOutbound withdrawalMoneyOutbound;
    private final GetAccountOutbound getAccountOutbound;
    private final SaveAccountOutbound saveAccountOutbound;
    private final WithdrawalStatusAccountRetry withdrawalStatusAccountRetry;

    @Override
    public AccountOperation withdraw(AccountWithdrawDto accountWithdrawDto) {
        AccountOperation accountOperation = saveOperationInBound.save(
                AccountOperation.builder()
                        .operationId(accountWithdrawDto.getOperationId())
                        .startedAt(LocalDateTime.now())
                        .accountIdA(accountWithdrawDto.getAccountIdA())
                        .accountIdB(accountWithdrawDto.getAccountIdB())
                        .amount(accountWithdrawDto.getAmount())
                        .status(OperationStatus.PROGRESS)
                        .type(OperationType.WITHDRAW)
                        .build()
        );

        eventPublisher.publish(ReserveAccountEvent.builder()
                .eventType(EventType.RESERVE)
                .accountIdA(accountWithdrawDto.getAccountIdA())
                .accountOperationId(accountOperation.getOperationId())
                .amount(accountWithdrawDto.getAmount())
                .build());

        eventPublisher.publish(WithdrawAccountEvent.builder()
                .eventType(EventType.WITHDRAW)
                .accountIdA(accountWithdrawDto.getAccountIdA())
                .accountOperationId(accountOperation.getOperationId())
                .amount(accountWithdrawDto.getAmount())
                .build());

        return accountOperation;
    }

    @Override
    public void requestWithdrawal(AccountId accountId, Amount withdrawalAmount, AccountOperationId accountOperationId) {
        try {
            AccountWithdrawTaskFunction<AccountOperationId, AccountId, Amount, Boolean> targetCall = getTargetCall();
            withdrawAccountRetry.retry(targetCall, accountOperationId, accountId, withdrawalAmount);

            WithdrawalStateTaskFunction<AccountOperationId, WithdrawTaskStatus> targetWithdrawStatusCall = withdrawalMoneyOutbound::getWithdrawState;
            WithdrawTaskStatus status = withdrawalStatusAccountRetry.retry(targetWithdrawStatusCall, accountOperationId);

            processWithdrawStatus(accountId, withdrawalAmount, accountOperationId, status);
        } catch (ReserveOperationRollback ex) {
            reserveRollbackOperation(accountId, withdrawalAmount, accountOperationId);
        }
    }

    private void reserveRollbackOperation(AccountId accountId, Amount withdrawalAmount, AccountOperationId accountOperationId) {
        saveOperationStatusOutbound.saveStatus(accountOperationId, OperationStatus.FAILED);
        getAccountOutbound.get(accountId).ifPresent(account -> {
            Account deposit = account.deposit(withdrawalAmount);
            saveAccountOutbound.save(deposit);
        });
    }

    private void processWithdrawStatus(AccountId accountId, Amount withdrawalAmount, AccountOperationId accountOperationId, WithdrawTaskStatus status) {
        switch (status) {
            case PROCESSING -> saveOperationStatusOutbound
                    .saveStatus(accountOperationId, OperationStatus.PROGRESS_FROZEN);
            case FAILED -> reserveRollbackOperation(accountId, withdrawalAmount, accountOperationId);
            case COMPLETED -> saveOperationStatusOutbound.saveStatus(accountOperationId, OperationStatus.COMPLETED);
        }
    }

    private AccountWithdrawTaskFunction<AccountOperationId, AccountId, Amount, Boolean> getTargetCall() {
        return (operationId, accId, withdrawAmount) -> {
            try {
                AccountOperation accountOperation = getOperationOutbound.get(operationId)
                        .orElseThrow(() -> new IllegalArgumentException("There is not account Operation Id " + operationId.operationId()));
                if (OperationStatus.RESERVED.equals(accountOperation.getStatus())) {
                    withdrawalMoneyOutbound.withdraw(operationId, accId, withdrawAmount);
                    return Boolean.TRUE;
                }
            } catch (Exception ex) {
                throw new ReserveOperationRollback();
            }

            return Boolean.FALSE;
        };
    }
}
