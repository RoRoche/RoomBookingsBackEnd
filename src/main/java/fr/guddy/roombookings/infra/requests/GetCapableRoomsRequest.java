package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.params.Parameter;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

public final class GetCapableRoomsRequest implements Request {

    private final Rooms rooms;
    private final int capacity;

    public GetCapableRoomsRequest(final Rooms rooms, final int capacity) {
        this.rooms = rooms;
        this.capacity = capacity;
    }

    public GetCapableRoomsRequest(final Rooms rooms, final Parameter<Integer> capacity) {
        this(
                rooms,
                capacity.value()
        );
    }

    @Override
    public void perform(final Context context) {
        final List<Room> capableRooms = rooms.capableRooms(capacity);
        if (capableRooms.isEmpty()) {
            context.status(HttpStatus.NO_CONTENT_204);
        } else {
            context.json(
                    capableRooms.stream()
                            .map(JsonRoom::new)
                            .map(JsonRoom::map)
                            .toList()
            ).status(HttpStatus.OK_200);
        }
    }
}
