package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.params.Parameter;
import fr.guddy.roombookings.infra.params.PathParameter;
import fr.guddy.roombookings.infra.params.RequiredParameter;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

public final class GetNamedRoomRequest implements Request {

    private final Rooms rooms;
    private final String name;

    public GetNamedRoomRequest(final Rooms rooms, final String name) {
        this.rooms = rooms;
        this.name = name;
    }

    public GetNamedRoomRequest(final Rooms rooms, final Context context) {
        this(
                rooms,
                new RequiredParameter<>(
                        new PathParameter("name", context)
                )
        );
    }

    public GetNamedRoomRequest(final Rooms rooms, final Parameter<String> name) {
        this(
                rooms,
                name.value()
        );
    }

    @Override
    public void perform(final Context context) {
        final Room room = rooms.withName(name).orElseThrow(() -> new RoomNotFoundException(name));
        context.json(new JsonRoom(room).map())
                .status(HttpStatus.OK_200);
    }
}
