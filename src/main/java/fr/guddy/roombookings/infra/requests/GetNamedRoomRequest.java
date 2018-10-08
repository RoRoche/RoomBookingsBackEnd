package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import io.javalin.Context;
import org.eclipse.jetty.http.HttpStatus;

public final class GetNamedRoomRequest implements Request {

    private final Rooms rooms;
    private final String name;

    public GetNamedRoomRequest(final Rooms rooms, final String name) {
        this.rooms = rooms;
        this.name = name;
    }

    @Override
    public void perform(final Context context) throws RoomNotFoundException {
        final Room room = rooms.namedRoom(name).orElseThrow(() -> new RoomNotFoundException(name));
        context.json(new JsonRoom(room).map())
                .status(HttpStatus.OK_200);
    }
}
