package domain.usecase;

import domain.exception.DepositOperationRollback;
import domain.exception.ReserveOperationRollback;
import domain.model.*;
import domain.port.inbound.DepositAccountInbound;
import domain.port.outbound.GetAccountOutbound;
import domain.port.outbound.GetOperationOutbound;
import domain.port.outbound.SaveAccountOutbound;
import domain.port.outbound.SaveOperationStatusOutbound;
import domain.usecase.util.AccountDepositTaskFunction;
import domain.usecase.util.DepositAccountRetry;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.ReadWriteLock;

@RequiredArgsConstructor
public class DepositAccountUseCase implements DepositAccountInbound {

    private final SaveAccountOutbound saveAccountOutbound;
    private final SaveOperationStatusOutbound saveOperationStatusOutbound;
    private final ReadWriteLock lock;
    private final GetOperationOutbound getOperationOutbound;
    private final DepositAccountRetry depositAccountRetry;
    private final GetAccountOutbound getAccountOutbound;

    @Override
    public void deposit(Account accountB, AccountId accountIdA, Amount depositAmount, AccountOperationId accountOperationId) {
        lock.writeLock().lock();
        try {
            AccountDepositTaskFunction<AccountOperationId, Account, Amount, Boolean> targetCall = getTargetCall();
            depositAccountRetry.retry(targetCall, accountOperationId, accountB, depositAmount);
        } catch (IllegalArgumentException ex) {
            //LOG
            System.out.println("LOG: There is no account Operation Id " + accountOperationId.operationId());
        } catch (DepositOperationRollback ex) {
            System.out.println("LOG: Starting deposit operation rollback for operation id" + accountOperationId.operationId());
            depositRollback(accountB, accountIdA, depositAmount, accountOperationId);
        } catch (ReserveOperationRollback ex) {
            System.out.println("LOG: Starting reserve operation rollback for operation id" + accountOperationId.operationId());
            reserveRollback(accountIdA, depositAmount, accountOperationId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void reserveRollback(AccountId accountIdA, Amount depositAmount, AccountOperationId accountOperationId) {
        try {
            saveOperationStatusOutbound.saveStatus(accountOperationId, OperationStatus.FAILED);
            getAccountOutbound.get(accountIdA).ifPresent(acc -> {
                Account depositAccountA = acc.deposit(depositAmount);
                saveAccountOutbound.save(depositAccountA);
            });
        } catch (Exception ex) {
            //LOG
            System.out.println("LOG: Failed reserve operation rollback for operation id" + accountOperationId.operationId());
        }
    }

    private void depositRollback(Account accountB, AccountId accountIdA, Amount depositAmount, AccountOperationId accountOperationId) {
        try {
            saveOperationStatusOutbound.saveStatus(accountOperationId, OperationStatus.FAILED);
            getAccountOutbound.get(accountB.getAccountId()).ifPresent(acc -> {
                Account depositAccountB = acc.reserve(depositAmount);
                saveAccountOutbound.save(depositAccountB);
            });
            getAccountOutbound.get(accountIdA).ifPresent(acc -> {
                Account depositAccountA = acc.deposit(depositAmount);
                saveAccountOutbound.save(depositAccountA);
            });
        } catch (Exception ex) {
            //LOG
            System.out.println("LOG: Failed deposit operation rollback for operation id" + accountOperationId.operationId());
        }
    }

    private AccountDepositTaskFunction<AccountOperationId, Account, Amount, Boolean> getTargetCall() {
        return (operationId, acc, depositAmount) -> {
            try {
                AccountOperation accountOperation = getOperationOutbound.get(operationId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "There is not account Operation Id " + operationId.operationId()));
                if (OperationStatus.RESERVED.equals(accountOperation.getStatus())) {
                    Account depositAccountB = acc.deposit(depositAmount);
                    saveAccountOutbound.save(depositAccountB);
                    saveAccountOperation(operationId);
                    return Boolean.TRUE;
                }
            } catch (DepositOperationRollback ex) {
                throw ex;
            } catch (Exception ex) {
                throw new ReserveOperationRollback();
            }
            return Boolean.FALSE;
        };
    }

    private void saveAccountOperation(AccountOperationId operationId) throws DepositOperationRollback {
        try {
            saveOperationStatusOutbound.saveStatus(operationId, OperationStatus.COMPLETED);
        } catch (Exception ex) {
            throw new DepositOperationRollback();
        }
    }
}
