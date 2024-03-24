package infrastructure.adapter.driving.validator;

import domain.dto.AccountTransferDto;

public class AccountTransferValidator {
    public static boolean isValid(AccountTransferDto accountTransferDto) {
        return accountTransferDto.isNotNull();
    }
}

