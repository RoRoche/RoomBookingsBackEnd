package fr.guddy.roombookings.domain.rooms;

import fr.guddy.roombookings.domain.room.Room;

import java.util.List;
import java.util.Optional;

public interface Rooms {
    void create(final Room room);

    List<Room> rooms();

    Optional<Room> namedRoom(final String name);
}
