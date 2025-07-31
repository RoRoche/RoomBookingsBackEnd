package fr.guddy.roombookings.domain.bookings;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.slot.Slot;

import java.util.List;
import java.util.Optional;

public interface Bookings {
    Long create(final Booking booking);

    List<Booking> forRoomInSlot(final Room room, final Slot slot);

    List<Booking> forUserFromStartDate(final String userId, final long timestampStart);

    boolean isConflicting(final Booking booking);

    int clearAll();

    Optional<Booking> byId(final long id);

    boolean delete(final Booking booking);
}
