package fr.guddy.roombookings.domain.booking;

import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.slot.Slot;

import java.util.Map;

public interface Booking {
    Long id();

    String userId();

    Room room();

    Slot slot();

    Map<String, Object> map();
}
