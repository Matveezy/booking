package booking.exception;

public class RoomIsNotFreeException extends RuntimeException{

    public RoomIsNotFreeException(String message) {
        super(message);
    }
}