package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.Rooms;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

public final class GetRoomsRequest implements Request {
    private final Rooms rooms;

    public GetRoomsRequest(final Rooms rooms) {
        this.rooms = rooms;
    }

    @Override
    public void perform(final Context context) {
        final List<Room> all = this.rooms.rooms();
        if (all.isEmpty()) {
            context.status(HttpStatus.NO_CONTENT_204);
        } else {
            context.json(
                    all.stream()
                            .map(JsonRoom::new)
                            .map(JsonRoom::map)
                            .toList()
            ).status(HttpStatus.OK_200);
        }
    }
}
