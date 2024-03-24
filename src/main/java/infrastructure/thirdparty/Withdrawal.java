package infrastructure.thirdparty;

import java.math.BigDecimal;

record Withdrawal(WithdrawalState state, long finaliseAt, Address address, BigDecimal amount) {
    public WithdrawalState finalState() {
        return finaliseAt <= System.currentTimeMillis() ? state : WithdrawalState.PROCESSING;
    }
}
