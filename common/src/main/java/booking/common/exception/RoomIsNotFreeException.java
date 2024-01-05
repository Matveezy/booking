package booking.common.exception;

public class RoomIsNotFreeException extends RuntimeException{

    public RoomIsNotFreeException(String message) {
        super(message);
    }
}
