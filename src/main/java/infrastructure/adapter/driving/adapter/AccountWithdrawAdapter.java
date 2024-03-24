package infrastructure.adapter.driving.adapter;

import domain.dto.AccountWithdrawDto;
import domain.port.inbound.WithdrawalMoneyInbound;
import infrastructure.adapter.driving.dto.AccountWithdrawResponse;
import infrastructure.adapter.driving.validator.AccountWithdrawValidator;
import infrastructure.mapper.AccountWithdrawMapper;
import infrastructure.mapper.AccountWithdrawResponseMapper;
import infrastructure.util.RequestUtil;
import infrastructure.util.ResponseUtil;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountWithdrawAdapter {

    private final WithdrawalMoneyInbound withdrawalMoneyInbound;
    private final AccountWithdrawMapper accountWithdrawMapper;
    private final AccountWithdrawResponseMapper accountWithdrawResponseMapper;

    public void withdrawMoney(Context ctx) {
        String body = RequestUtil.getBody(ctx);

        AccountWithdrawDto accountWithdrawDto = accountWithdrawMapper.toAccountWithdrawDto(body);

        if (!AccountWithdrawValidator.isValid(accountWithdrawDto)) {
            ResponseUtil.returnInvalidRequestBodyResponse(ctx);
        }

        var accountOperation = withdrawalMoneyInbound.withdraw(accountWithdrawDto);

        AccountWithdrawResponse response = accountWithdrawResponseMapper.toAccountWithdrawResponse(accountOperation);
        ResponseUtil.returnAccountWithdrawResponse(ctx, response);
    }
}
