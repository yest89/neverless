package domain.usecase.util;

import domain.exception.ReserveOperationRollback;

public interface AccountWithdrawTaskFunction<T, U, F, R> {
    R apply(T t, U u, F f) throws ReserveOperationRollback;
}
