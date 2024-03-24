package domain.dto;

import domain.model.AccountId;
import domain.model.AccountOperationId;
import domain.model.Amount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountTransferDto implements NotEmptyObject {

    private AccountOperationId operationId;

    private AccountId accountIdA;

    private AccountId accountIdB;

    private Amount amount;

    @Override
    public boolean isNotNull() {
        return accountIdA.isNotNull() &&
                accountIdB.isNotNull() &&
                amount.isNotNull();
    }
}
