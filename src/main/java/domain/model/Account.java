package domain.model;

import domain.dto.NotEmptyObject;
import domain.exception.NegativeAmountException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.EqualsExclude;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class Account implements NotEmptyObject {
    AccountId accountId;
    @EqualsExclude
    Amount amount;

    public Account(AccountId accountId, Amount amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    public boolean isNotNull() {
        return accountId.isNotNull() && amount.isNotNull();
    }

    public Account reserve(Amount amount) {
        Account account = new Account(this.getAccountId(), this.amount);
        BigDecimal reservedAmount = account.amount.amount().subtract(amount.amount());
        if (isNegative(reservedAmount)) {
            throw new NegativeAmountException(
                    String.format("Attempted to reserve a negative amount for account id %s", accountId.accountId()));
        }
        account.setAmount(new Amount(reservedAmount));
        return account;
    }

    public Account deposit(Amount amount) {
        Account account = new Account(this.getAccountId(), this.amount);
        BigDecimal depositAmount = account.amount.amount().add(amount.amount());
        account.setAmount(new Amount(depositAmount));
        return account;
    }

    private boolean isNegative(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

}
