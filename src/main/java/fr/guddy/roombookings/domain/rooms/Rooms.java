package fr.guddy.roombookings.domain.rooms;

import fr.guddy.roombookings.domain.room.Room;

import java.util.List;
import java.util.Optional;

public interface Rooms {
    Long create(final Room room);

    List<Room> all();

    List<Room> withCapacity(final int capacity);

    Optional<Room> withName(final String name);

    int clearAll();
}
