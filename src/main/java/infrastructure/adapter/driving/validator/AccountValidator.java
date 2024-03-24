package infrastructure.adapter.driving.validator;

import domain.model.Account;

import java.util.Optional;

public class AccountValidator {
    public static boolean isNotValid(Optional<Account> account) {
        return account.isEmpty();
    }
}

