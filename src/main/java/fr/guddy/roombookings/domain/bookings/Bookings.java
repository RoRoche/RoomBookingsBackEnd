package fr.guddy.roombookings.domain.bookings;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.slot.Slot;

import java.util.List;

public interface Bookings {
    void create(final Booking booking);

    List<Booking> bookings();

    List<Booking> bookingsForRoomInSlot(final Room room, final Slot slot);
}
