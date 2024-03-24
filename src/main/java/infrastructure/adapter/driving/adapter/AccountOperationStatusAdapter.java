package infrastructure.adapter.driving.adapter;

import domain.model.AccountOperationIdProvider;
import domain.port.outbound.GetOperationOutbound;
import infrastructure.adapter.driving.dto.OperationStatusResponse;
import infrastructure.adapter.driving.validator.OperationValidator;
import infrastructure.util.RequestUtil;
import infrastructure.util.ResponseUtil;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountOperationStatusAdapter {

    private final GetOperationOutbound getOperationOutbound;

    public void fetchStatus(Context ctx) {
        var operationNumber = RequestUtil.getOperationNumber(ctx);
        var accOperation = getOperationOutbound.get(AccountOperationIdProvider.toId(operationNumber));

        if (OperationValidator.isNotValid(accOperation)) {
            ResponseUtil.returnEmptyOperationStatusResponse(ctx);
            return;
        }

        var accountOperation = accOperation.get();
        OperationStatusResponse operationStatusResponse = new OperationStatusResponse(
                accountOperation.getOperationId().operationId().toString(),
                accountOperation.getStatus().name(),
                accountOperation.getAccountIdA().accountId(),
                accountOperation.getAccountIdB().accountId(),
                accountOperation.getAmount().amount(),
                accountOperation.getType().name());
        ResponseUtil.returnOperationStatusResponse(ctx, operationStatusResponse);
    }

}
