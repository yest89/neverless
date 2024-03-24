package infrastructure.configuration;

public class Path {

    public static class Web {
        public static final String BASE = "/api/v1/neverless";
        public static final String ACCOUNT_TRANSFER = BASE + "/accounts/transfer";
        public static final String ACCOUNT_WITHDRAW = BASE + "/accounts/withdraw";
        public static final String ACCOUNT_OPERATION = BASE + "/operations/operation/{operationNumber}";
        public static final String ACCOUNT = BASE + "/accounts/account/{accountNumber}";
    }
}
