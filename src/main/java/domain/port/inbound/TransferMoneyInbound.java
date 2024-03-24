package domain.port.inbound;

import domain.dto.AccountTransferDto;
import domain.model.AccountOperation;

public interface TransferMoneyInbound {
    AccountOperation transfer(AccountTransferDto accountTransferDto);

}
