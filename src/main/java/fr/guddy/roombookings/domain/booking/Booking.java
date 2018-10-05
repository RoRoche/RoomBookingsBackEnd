package fr.guddy.roombookings.domain.bookings;

import fr.guddy.roombookings.domain.rooms.Room;
import fr.guddy.roombookings.domain.slots.Slot;

public interface Booking {
    Slot slot();

    Room room();
}
