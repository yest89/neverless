package domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AccountOperation {
    AccountOperationId operationId;
    AccountId accountIdA;
    AccountId accountIdB;
    Amount amount;
    OperationStatus status;
    OperationType type;
    LocalDateTime startedAt;
    LocalDateTime endedAt;
}
