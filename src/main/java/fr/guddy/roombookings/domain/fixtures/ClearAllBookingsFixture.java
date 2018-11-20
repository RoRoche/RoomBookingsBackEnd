package fr.guddy.roombookings.domain.fixtures;

import fr.guddy.roombookings.domain.bookings.Bookings;
import org.dizitart.no2.WriteResult;

public final class ClearAllBookingsFixture implements Fixture {
    private final Bookings bookings;

    public ClearAllBookingsFixture(final Bookings bookings) {
        this.bookings = bookings;
    }

    @Override
    public WriteResult perform() {
        return bookings.clearAll();
    }
}
