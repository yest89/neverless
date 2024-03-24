package domain.port.outbound;

import domain.model.AccountOperation;
import domain.model.AccountOperationId;

import java.util.Optional;

public interface GetOperationOutbound {
    Optional<AccountOperation> get(AccountOperationId operationId);
}
