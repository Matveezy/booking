package booking.exception;

public class RoomNotFoundException extends EntityNotFoundException {
    public RoomNotFoundException(long id) {
        super("Room with id=" + id + " not found");
    }
}
