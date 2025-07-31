package fr.guddy.roombookings.domain.booking;

import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.slot.Slot;

import java.util.HashMap;
import java.util.Map;

public record SimpleBooking(Long id, String userId, Room room, Slot slot) implements Booking {

    @Override
    public Map<String, Object> map() {
        final Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("room", room);
        map.put("slot", slot);
        return map;
    }
}
