package fr.guddy.roombookings.domain.booking;

import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.slot.Slot;

import java.util.HashMap;
import java.util.Map;

public final class SimpleBooking implements Booking {
    private final Long id;
    private final String userId;
    private final Room room;
    private final Slot slot;

    public SimpleBooking(final Long id, final String userId, final Room room, final Slot slot) {
        this.id = id;
        this.userId = userId;
        this.room = room;
        this.slot = slot;
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public String userId() {
        return userId;
    }

    @Override
    public Room room() {
        return room;
    }

    @Override
    public Slot slot() {
        return slot;
    }

    @Override
    public Map<String, Object> map() {
        final Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("room", room);
        map.put("slot", slot);
        return map;
    }
}
