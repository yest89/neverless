package infrastructure.thirdparty;


public interface WithdrawalService {
    /**
     * Request a withdrawal for given address and amount. Completes at random moment between 1 and 10 seconds
     *
     * @param id      - a caller generated withdrawal id, used for idempotency
     * @param address - an address withdraw to, can be any arbitrary string
     * @param amount  - an amount to withdraw (please replace T with type you want to use)
     * @throws IllegalArgumentException in case there's different address or amount for given id
     */
    void requestWithdrawal(WithdrawalId id, Address address, Amount amount);

    /**
     * Return current state of withdrawal
     *
     * @param id - a withdrawal id
     * @return current state of withdrawal
     * @throws IllegalArgumentException in case there no withdrawal for the given id
     */
    WithdrawalState getRequestState(WithdrawalId id);
}
