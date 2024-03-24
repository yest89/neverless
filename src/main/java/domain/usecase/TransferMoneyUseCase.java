package domain.usecase;

import domain.dto.AccountTransferDto;
import domain.model.AccountOperation;
import domain.model.OperationStatus;
import domain.model.OperationType;
import domain.port.inbound.SaveOperationInbound;
import domain.port.inbound.TransferMoneyInbound;
import domain.port.outbound.model.DepositAccountEvent;
import domain.port.outbound.model.ReserveAccountEvent;
import domain.port.outbound.model.EventType;
import infrastructure.adapter.driven.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TransferMoneyUseCase implements TransferMoneyInbound {
    private final SaveOperationInbound saveOperationInBound;
    private final EventPublisher eventPublisher;

    public AccountOperation transfer(AccountTransferDto accountTransferDto) {
        AccountOperation accountOperation = saveOperationInBound.save(
                AccountOperation.builder()
                        .operationId(accountTransferDto.getOperationId())
                        .startedAt(LocalDateTime.now())
                        .accountIdA(accountTransferDto.getAccountIdA())
                        .accountIdB(accountTransferDto.getAccountIdB())
                        .amount(accountTransferDto.getAmount())
                        .status(OperationStatus.PROGRESS)
                        .type(OperationType.P2P)
                        .build()
        );

        ReserveAccountEvent reserveEvent = ReserveAccountEvent.builder()
                .eventType(EventType.RESERVE)
                .accountIdA(accountTransferDto.getAccountIdA())
                .accountOperationId(accountOperation.getOperationId())
                .amount(accountTransferDto.getAmount())
                .build();
        eventPublisher.publish(reserveEvent);

        DepositAccountEvent depositEvent = DepositAccountEvent.builder()
                .eventType(EventType.DEPOSIT)
                .accountIdA(accountTransferDto.getAccountIdA())
                .accountIdB(accountTransferDto.getAccountIdB())
                .accountOperationId(accountOperation.getOperationId())
                .amount(accountTransferDto.getAmount())
                .build();
        eventPublisher.publish(depositEvent);

        return accountOperation;
    }
}
