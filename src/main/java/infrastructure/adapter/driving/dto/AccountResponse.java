package infrastructure.adapter.driving.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponse {

    @JsonProperty("accountId")
    private final String accountId;

    @JsonProperty("amount")
    private final BigDecimal amount;

    public AccountResponse(String accountId, BigDecimal amount) {
        this.accountId = accountId;
        this.amount = amount;
    }
}
