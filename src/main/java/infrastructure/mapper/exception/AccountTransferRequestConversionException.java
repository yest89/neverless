package infrastructure.mapper.exception;

public class AccountTransferRequestConversionException extends RuntimeException {
    public AccountTransferRequestConversionException(String message, Throwable throwable) {
        super(message);
    }
}
