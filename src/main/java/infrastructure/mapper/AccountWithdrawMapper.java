package infrastructure.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.dto.AccountWithdrawDto;
import domain.model.AccountId;
import domain.model.Amount;
import infrastructure.mapper.exception.AccountTransferRequestConversionException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class AccountWithdrawMapper {
    private final ObjectMapper objectMapper;

    public AccountWithdrawDto toAccountWithdrawDto(String body) {
        String accountANumber = StringUtils.EMPTY;
        String accountBNumber = StringUtils.EMPTY;
        try {
            JsonNode jsonNode = objectMapper.readTree(body);

            accountANumber = jsonNode.path("accountANumber").asText();
            accountBNumber = jsonNode.path("accountBNumber").asText();
            String amount = jsonNode.path("amount").asText();

            return AccountWithdrawDto.builder()
                    .accountIdA(new AccountId(accountANumber))
                    .accountIdB(new AccountId(accountBNumber))
                    .amount(new Amount(new BigDecimal(amount)))
                    .build();

        } catch (JsonProcessingException e) {
            throw new AccountTransferRequestConversionException(
                    String.format("Error converting JSON to AccountWithdrawDto for accountANumber %s and for accountBNumber %s",
                            accountANumber, accountBNumber), e);
        }
    }
}
