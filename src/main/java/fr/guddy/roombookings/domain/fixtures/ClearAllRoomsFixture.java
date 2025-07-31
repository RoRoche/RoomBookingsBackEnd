package fr.guddy.roombookings.domain.fixtures;

import fr.guddy.roombookings.domain.rooms.Rooms;

public final class ClearAllRoomsFixture implements Fixture {
    private final Rooms rooms;

    public ClearAllRoomsFixture(final Rooms rooms) {
        this.rooms = rooms;
    }

    @Override
    public Long perform() {
        return (long) rooms.clearAll();
    }
}
