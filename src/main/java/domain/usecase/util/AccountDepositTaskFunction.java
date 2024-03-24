package domain.usecase.util;

import domain.exception.DepositOperationRollback;
import domain.exception.ReserveOperationRollback;

public interface AccountDepositTaskFunction<T, U, F, R> {
    R apply(T t, U u, F f) throws DepositOperationRollback, ReserveOperationRollback;
}
