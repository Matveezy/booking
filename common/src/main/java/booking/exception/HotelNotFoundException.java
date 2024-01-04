package booking.exception;

public class HotelNotFoundException extends EntityNotFoundException {
    public HotelNotFoundException(long id) {
        super("Hotel with id=" + id + " not found");
    }
}
