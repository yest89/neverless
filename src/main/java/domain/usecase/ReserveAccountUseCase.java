package domain.usecase;

import domain.model.Account;
import domain.model.AccountOperationId;
import domain.model.Amount;
import domain.model.OperationStatus;
import domain.port.inbound.ReserveAccountInbound;
import domain.port.outbound.SaveAccountOutbound;
import domain.port.outbound.SaveOperationStatusOutbound;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.ReadWriteLock;

@RequiredArgsConstructor
public class ReserveAccountUseCase implements ReserveAccountInbound {
    private final SaveAccountOutbound saveAccountOutbound;
    private final SaveOperationStatusOutbound saveOperationStatusOutbound;
    private final ReadWriteLock lock;

    public void reserve(Account account, Amount reservedAmount, AccountOperationId accountOperationId) {
        lock.writeLock().lock();
        try {
            var reserveAccount = account.reserve(reservedAmount);
            saveAccountOutbound.save(reserveAccount);
            saveOperationStatusOutbound.saveStatus(accountOperationId, OperationStatus.RESERVED);
        } catch (Exception ex) {
            saveOperationStatusOutbound.saveStatus(accountOperationId, OperationStatus.FAILED);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
