package infrastructure.adapter.driving.validator;

import domain.model.AccountOperation;

import java.util.Optional;

public class OperationValidator {
    public static boolean isNotValid(Optional<AccountOperation> operation) {
        return operation.isEmpty();
    }
}

