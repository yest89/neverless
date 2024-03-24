package domain.model;

import domain.dto.NotEmptyObject;

import java.math.BigDecimal;

public record Amount(BigDecimal amount)  implements NotEmptyObject {

    public boolean isNotNull() {
        return amount != null;
    }
}
