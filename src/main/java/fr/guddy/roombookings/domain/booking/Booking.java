package fr.guddy.roombookings.domain.booking;

import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.slots.Slot;

import java.util.Map;

public interface Booking {
    Long id();

    Room room();

    Slot slot();

    Map<String, Object> map();
}
