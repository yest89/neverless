package infrastructure.adapter.driven.adapter;

import domain.model.AccountOperation;
import domain.model.AccountOperationId;
import domain.model.OperationStatus;
import domain.port.inbound.SaveOperationInbound;
import domain.port.outbound.SaveOperationStatusOutbound;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

@RequiredArgsConstructor
public class InMemoryOperationAdapter implements SaveOperationStatusOutbound, SaveOperationInbound {
    private final ConcurrentMap<AccountOperationId, AccountOperation> operations;

    @Override
    public void saveStatus(AccountOperationId operationId, OperationStatus operationStatus) {
        operations.computeIfPresent(operationId, (accountOperationId, accountOperation) -> {
            accountOperation.setStatus(operationStatus);
            if (OperationStatus.COMPLETED.equals(operationStatus)) {
                accountOperation.setEndedAt(LocalDateTime.now());
            }
            return accountOperation;

        });
    }

    @Override
    public AccountOperation save(AccountOperation accountOperation) {
        accountOperation.setOperationId(new AccountOperationId(UUID.randomUUID()));
        operations.put(accountOperation.getOperationId(), accountOperation);
        return accountOperation;
    }
}
