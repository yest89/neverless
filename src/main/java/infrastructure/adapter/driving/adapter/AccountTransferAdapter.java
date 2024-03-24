package infrastructure.adapter.driving.adapter;

import domain.dto.AccountTransferDto;
import domain.port.inbound.TransferMoneyInbound;
import infrastructure.adapter.driving.validator.AccountTransferValidator;
import infrastructure.mapper.AccountTransferMapper;
import infrastructure.mapper.AccountTransferResponseMapper;
import infrastructure.util.RequestUtil;
import infrastructure.util.ResponseUtil;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountTransferAdapter {

    private final TransferMoneyInbound transferMoneyInbound;
    private final AccountTransferMapper accountTransferMapper;
    private final AccountTransferResponseMapper accountTransferResponseMapper;

    public void transferMoney(Context ctx) {
        String body = RequestUtil.getBody(ctx);

        AccountTransferDto accountTransferDto = accountTransferMapper.toAccountTransferDto(body);

        if (!AccountTransferValidator.isValid(accountTransferDto)) {
            ResponseUtil.returnInvalidRequestBodyResponse(ctx);
        }

        var accountOperation = transferMoneyInbound.transfer(accountTransferDto);

        var response = accountTransferResponseMapper.toAccountTransferResponse(accountOperation);
        ResponseUtil.returnAccountTransferResponse(ctx, response);
    }

}
