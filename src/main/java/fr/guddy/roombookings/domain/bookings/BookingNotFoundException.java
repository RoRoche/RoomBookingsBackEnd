package fr.guddy.roombookings.domain.bookings;

public final class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(final long id) {
        super(String.format("No booking with id %d were found", id));
    }
}
