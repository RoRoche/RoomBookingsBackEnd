package fr.guddy.roombookings.domain.fixtures;

import fr.guddy.roombookings.domain.bookings.Bookings;

public final class ClearAllBookingsFixture implements Fixture<Integer> {
    private final Bookings bookings;

    public ClearAllBookingsFixture(final Bookings bookings) {
        this.bookings = bookings;
    }

    @Override
    public Integer perform() {
        return bookings.clearAll();
    }
}
