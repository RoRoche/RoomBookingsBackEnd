package fr.guddy.roombookings.domain.fixtures;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.bookings.Bookings;

public final class CreateBookingFixture implements Fixture {
    private final Bookings bookings;
    private final Booking booking;

    public CreateBookingFixture(final Bookings bookings, final Booking booking) {
        this.bookings = bookings;
        this.booking = booking;
    }

    @Override
    public Long perform() {
        return bookings.create(booking);
    }
}
