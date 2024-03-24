package domain.model;

import domain.dto.NotEmptyObject;

import java.util.UUID;

public record AccountOperationId(UUID operationId) implements NotEmptyObject {

    public boolean isNotNull() {
        return operationId != null;
    }
}
