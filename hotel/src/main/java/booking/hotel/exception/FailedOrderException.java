package booking.hotel.exception;

public class FailedOrderException extends RuntimeException {

    public FailedOrderException(String message) {
        super(message);
    }
}
