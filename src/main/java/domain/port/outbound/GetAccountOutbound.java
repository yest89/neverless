package domain.port.outbound;

import domain.model.Account;
import domain.model.AccountId;

import java.util.Optional;

public interface GetAccountOutbound {

    Optional<Account> get(AccountId accountAId);
}
