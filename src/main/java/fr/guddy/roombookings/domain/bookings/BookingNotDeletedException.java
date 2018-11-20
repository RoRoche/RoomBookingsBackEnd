package fr.guddy.roombookings.domain.bookings;

public final class BookingNotDeletedException extends RuntimeException {
    public BookingNotDeletedException(final long id) {
        super(
                String.format("Booking with id %d could not be deleted", id)
        );
    }
}
