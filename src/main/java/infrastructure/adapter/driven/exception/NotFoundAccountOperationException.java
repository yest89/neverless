package infrastructure.adapter.driven.exception;

public class NotFoundAccountOperationException extends Exception {
    public NotFoundAccountOperationException(String message) {
        super(message);
    }
}
