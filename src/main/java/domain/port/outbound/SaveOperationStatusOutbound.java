package domain.port.outbound;

import domain.model.AccountOperationId;
import domain.model.OperationStatus;

public interface SaveOperationStatusOutbound {

    void saveStatus(AccountOperationId operationId, OperationStatus operationStatus);
}
