package infrastructure.adapter.query;

import domain.model.Account;
import domain.model.AccountId;
import domain.port.outbound.GetAccountOutbound;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@RequiredArgsConstructor
public class InMemoryAccountQueryAdapter implements GetAccountOutbound {

    private final ConcurrentMap<AccountId, Account> accounts;

    @Override
    public Optional<Account> get(AccountId accountId) {
        Account account = accounts.get(accountId);
        if (account == null) {
            return Optional.empty();
        }
        return Optional.of(new Account(account.getAccountId(), account.getAmount()));
    }
}
