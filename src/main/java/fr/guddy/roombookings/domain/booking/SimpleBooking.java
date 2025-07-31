package fr.guddy.roombookings.domain.booking;

import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.slot.Slot;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;

import java.util.Map;

public record SimpleBooking(Long id, String userId, Room room, Slot slot) implements Booking {

    @Override
    public Map<String, Object> map() {
        return new MapOf<>(
                new MapEntry<>("id", id),
                new MapEntry<>("room", room),
                new MapEntry<>("slot", slot)
        );
    }
}
