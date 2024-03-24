package domain.usecase.util;

import infrastructure.adapter.driven.exception.WithdrawalStatusException;

public interface WithdrawalStateTaskFunction<T, R> {
    R apply(T t) throws WithdrawalStatusException;
}
