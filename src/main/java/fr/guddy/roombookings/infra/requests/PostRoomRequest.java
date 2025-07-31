package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.CreateRoomConflictException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import io.javalin.http.Context;
import org.dizitart.no2.exceptions.UniqueConstraintException;
import org.eclipse.jetty.http.HttpStatus;

public final class PostRoomRequest implements Request {
    private final Rooms rooms;
    private final Room room;

    public PostRoomRequest(final Rooms rooms, final Room room) {
        this.rooms = rooms;
        this.room = room;
    }

    public PostRoomRequest(final Rooms rooms, final Context context) {
        this(
                rooms,
                new JsonRoom(context.body())
        );
    }

    @Override
    public void perform(final Context context) {
        try {
            rooms.create(room);
        } catch (final UniqueConstraintException exception) {
            throw new CreateRoomConflictException(exception, room.name());
        }
        context.header("location", String.format("/rooms/%s", room.name()))
                .status(HttpStatus.CREATED_201);
    }
}
