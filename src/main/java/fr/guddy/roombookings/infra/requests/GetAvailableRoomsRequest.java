package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.domain.slot.Slot;
import fr.guddy.roombookings.infra.params.*;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

import java.util.List;

public final class GetAvailableRoomsRequest implements Request {

    private final Rooms rooms;
    private final Bookings bookings;
    private final int capacity;
    private final Slot slot;

    public GetAvailableRoomsRequest(final Rooms rooms, final Bookings bookings, final int capacity, final Slot slot) {
        this.rooms = rooms;
        this.bookings = bookings;
        this.capacity = capacity;
        this.slot = slot;
    }

    public GetAvailableRoomsRequest(final Rooms rooms,
                                    final Bookings bookings,
                                    final Parameter<Integer> capacity,
                                    final Parameter<Long> timestampStart,
                                    final Parameter<Long> timestampEnd) {
        this(
                rooms,
                bookings,
                capacity.value(),
                new LogicalSlot(
                        timestampStart.value(),
                        timestampEnd.value()
                )
        );
    }

    public GetAvailableRoomsRequest(final Rooms rooms, final Bookings bookings, final Context context) {
        this(
                rooms,
                bookings,
                new IntegerParameter(
                        new RequiredParameter<>(
                                new QueryParameter("capacity", context)
                        )
                ),
                new LongParameter(
                        new RequiredParameter<>(
                                new QueryParameter("timestamp_start", context)
                        )
                ),
                new LongParameter(
                        new RequiredParameter<>(
                                new QueryParameter("timestamp_end", context)
                        )
                )
        );
    }

    @Override
    public void perform(final Context context) {
        final List<Room> availableRooms = rooms.withCapacity(capacity)
                .stream()
                .filter(room ->
                        bookings.forRoomInSlot(room, slot).isEmpty()
                ).toList();
        if (availableRooms.isEmpty()) {
            context.status(HttpStatus.NO_CONTENT_204);
        } else {
            context.json(
                    availableRooms.stream()
                            .map(JsonRoom::new)
                            .map(JsonRoom::map)
                            .toList()
            ).status(HttpStatus.OK_200);
        }
    }
}
