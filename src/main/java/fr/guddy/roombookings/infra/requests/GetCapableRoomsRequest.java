package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.Rooms;
import io.javalin.Context;
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

    @Override
    public void perform(final Context context) {
        final List<Room> all = rooms.capableRooms(capacity);
        if (all.isEmpty()) {
            context.status(HttpStatus.NO_CONTENT_204);
        } else {
            context.json(
                    all.stream()
                            .map(JsonRoom::new)
                            .map(JsonRoom::map)
                            .collect(Collectors.toList())
            ).status(HttpStatus.OK_200);
        }
    }
}
