package infrastructure.adapter.driving.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperationStatusResponse {

    @JsonProperty("operationNumber")
    private final String operationNumber;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("accountIdA")
    private final String accountIdA;

    @JsonProperty("accountIdB")
    private final String accountIdB;

    @JsonProperty("amount")
    private final BigDecimal amount;

    @JsonProperty("type")
    private final String type;

}
