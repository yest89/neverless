package domain.model;

import domain.dto.NotEmptyObject;
import org.apache.commons.lang3.StringUtils;

public record AccountId(String accountId) implements NotEmptyObject {

    public boolean isNotNull() {
        return StringUtils.isNotBlank(accountId);
    }
}
