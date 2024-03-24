package domain.model;

import java.util.UUID;

public class AccountOperationIdProvider {

    public static AccountOperationId toId(String id) {
        return new AccountOperationId(UUID.fromString(id));
    }

}
