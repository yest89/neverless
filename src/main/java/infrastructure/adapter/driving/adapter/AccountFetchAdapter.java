package infrastructure.adapter.driving.adapter;

import domain.model.AccountId;
import domain.port.outbound.GetAccountOutbound;
import infrastructure.adapter.driving.dto.AccountResponse;
import infrastructure.adapter.driving.validator.AccountValidator;
import infrastructure.util.RequestUtil;
import infrastructure.util.ResponseUtil;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountFetchAdapter {
    private final GetAccountOutbound getAccountOutbound;

    public void fetchAccount(Context ctx) {
        var accountNumber = RequestUtil.getAccountNumber(ctx);

        var acc = getAccountOutbound.get(new AccountId(accountNumber));
        if (AccountValidator.isNotValid(acc)) {
            ResponseUtil.returnInvalidRequestBodyResponse(ctx);
        }

        var account = acc.get();
        AccountResponse response = new AccountResponse(account.getAccountId().accountId(), account.getAmount().amount());
        ResponseUtil.returnAccountResponse(ctx, response);
    }
}
