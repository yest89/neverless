package domain.port.outbound;

import domain.model.Account;

public interface SaveAccountOutbound {

    void save(Account account);
}
