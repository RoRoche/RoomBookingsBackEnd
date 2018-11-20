package fr.guddy.roombookings.domain.fixtures;

import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.Rooms;

public final class CreateRoomFixture implements Fixture {
    private final Rooms rooms;
    private final Room room;

    public CreateRoomFixture(final Rooms rooms, final Room room) {
        this.rooms = rooms;
        this.room = room;
    }

    @Override
    public void perform() {
        rooms.create(room);
    }
}
