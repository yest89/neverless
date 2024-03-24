package infrastructure.adapter.driven.adapter;

import domain.model.Account;
import domain.model.AccountId;
import domain.port.outbound.SaveAccountOutbound;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentMap;


@RequiredArgsConstructor
public class InMemoryAccountAdapter implements SaveAccountOutbound {

    private final ConcurrentMap<AccountId, Account> accounts;

    @Override
    public void save(Account account) {
        accounts.put(account.getAccountId(), account);
    }
}
