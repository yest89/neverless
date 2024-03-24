package infrastructure.adapter.driving.validator;

import domain.dto.AccountWithdrawDto;

public class AccountWithdrawValidator {
    public static boolean isValid(AccountWithdrawDto accountWithdrawDto) {
        return accountWithdrawDto.isNotNull();
    }
}

