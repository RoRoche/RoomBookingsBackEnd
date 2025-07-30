package fr.guddy.roombookings.domain.fixtures;

import fr.guddy.roombookings.domain.bookings.Bookings;

public final class ClearAllBookingsFixture implements Fixture<Boolean> {
    private final Bookings bookings;

    public ClearAllBookingsFixture(final Bookings bookings) {
        this.bookings = bookings;
    }

    @Override
    public Boolean perform() {
        return bookings.clearAll();
    }
}
