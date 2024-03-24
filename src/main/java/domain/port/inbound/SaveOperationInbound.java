package domain.port.inbound;

import domain.model.AccountOperation;

public interface SaveOperationInbound {

    AccountOperation save(AccountOperation accountOperation);
}
