package infrastructure.mapper;

import domain.model.AccountOperation;
import infrastructure.adapter.driving.dto.AccountWithdrawResponse;

public class AccountWithdrawResponseMapper {

    public AccountWithdrawResponse toAccountWithdrawResponse(AccountOperation accountOperation) {
        return AccountWithdrawResponse.builder()
                .accountIdA(accountOperation.getAccountIdA().accountId())
                .accountIdB(accountOperation.getAccountIdB().accountId())
                .operationNumber(accountOperation.getOperationId().operationId().toString())
                .status(accountOperation.getStatus().name())
                .amount(accountOperation.getAmount().amount())
                .operationType(accountOperation.getType().name())
                .build();
    }
}
