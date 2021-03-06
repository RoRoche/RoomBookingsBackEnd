package fr.guddy.roombookings.domain.fixtures;

import fr.guddy.roombookings.domain.rooms.Rooms;
import org.dizitart.no2.WriteResult;

public final class ClearAllRoomsFixture implements Fixture {
    private final Rooms rooms;

    public ClearAllRoomsFixture(final Rooms rooms) {
        this.rooms = rooms;
    }

    @Override
    public WriteResult perform() {
        return rooms.clearAll();
    }
}
