package fr.guddy.roombookings.domain.bookings;

public final class CreateBookingConflictException extends RuntimeException {
    public CreateBookingConflictException(final String roomName) {
        super(String.format("Room named '%s' already booked on this slot", roomName));
    }
}
