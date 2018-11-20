package fr.guddy.roombookings.domain.fixtures;

import fr.guddy.roombookings.domain.rooms.Rooms;

public final class ClearRoomsFixture implements Fixture {
    private final Rooms rooms;

    public ClearRoomsFixture(final Rooms rooms) {
        this.rooms = rooms;
    }

    @Override
    public void perform() {
        rooms.clearAll();
    }
}
