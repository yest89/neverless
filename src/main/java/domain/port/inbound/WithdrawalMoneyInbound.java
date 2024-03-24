package domain.port.inbound;

import domain.dto.AccountWithdrawDto;
import domain.model.*;

public interface WithdrawalMoneyInbound {
    AccountOperation withdraw(AccountWithdrawDto accountWithdrawDto);

    void requestWithdrawal(AccountId accountId, Amount amount, AccountOperationId accountOperationId);
}
