package infrastructure.adapter.query;

import domain.model.AccountOperation;
import domain.model.AccountOperationId;
import domain.port.outbound.GetOperationOutbound;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@RequiredArgsConstructor
public class InMemoryOperationQueryAdapter implements GetOperationOutbound {

    private final ConcurrentMap<AccountOperationId, AccountOperation> operations;

    @Override
    public Optional<AccountOperation> get(AccountOperationId operationId) {
        return Optional.ofNullable(operations.get(operationId));
    }
}
