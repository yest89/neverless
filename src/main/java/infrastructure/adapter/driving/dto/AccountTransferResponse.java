package infrastructure.adapter.driving.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountTransferResponse {
    @JsonProperty("operationNumber")
    private final String operationNumber;

    @JsonProperty("accountIdA")
    private final String accountIdA;

    @JsonProperty("accountIdB")
    private final String accountIdB;

    @JsonProperty("amount")
    private final BigDecimal amount;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("operationType")
    private final String operationType;

    public AccountTransferResponse(String operationNumber, String accountIdA, String accountIdB, BigDecimal amount, String status, String operationType) {
        this.operationNumber = operationNumber;
        this.accountIdA = accountIdA;
        this.accountIdB = accountIdB;
        this.amount = amount;
        this.status = status;
        this.operationType = operationType;
    }
}
