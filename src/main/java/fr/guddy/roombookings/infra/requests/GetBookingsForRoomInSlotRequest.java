package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.booking.JsonBooking;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.domain.slot.Slot;
import fr.guddy.roombookings.infra.params.*;
import io.javalin.Context;
import org.eclipse.jetty.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

public final class GetBookingsForRoomInSlotRequest implements Request {
    private final Bookings bookings;
    private final Room room;
    private final Slot slot;

    public GetBookingsForRoomInSlotRequest(final Bookings bookings, final Room room, final Slot slot) {
        this.bookings = bookings;
        this.room = room;
        this.slot = slot;
    }

    public GetBookingsForRoomInSlotRequest(
            final Rooms rooms,
            final Bookings bookings,
            final Context context) {
        this(
                rooms,
                bookings,
                new RequiredParameter<>(
                        new PathParameter("name", context)
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

    public GetBookingsForRoomInSlotRequest(
            final Rooms rooms,
            final Bookings bookings,
            final Parameter<String> roomName,
            final Parameter<Long> timestampStart,
            final Parameter<Long> timestampEnd
    ) {
        this(
                bookings,
                rooms.namedRoom(roomName.value()).orElseThrow(() -> new RoomNotFoundException(roomName.value())),
                new LogicalSlot(
                        timestampStart.value(),
                        timestampEnd.value()
                )
        );
    }

    @Override
    public void perform(final Context context) {
        final List<Booking> existingBookings = bookings.bookingsForRoomInSlot(room, slot);
        if (existingBookings.isEmpty()) {
            context.status(HttpStatus.NO_CONTENT_204);
        } else {
            context.json(
                    existingBookings.stream()
                            .map(JsonBooking::new)
                            .map(JsonBooking::map)
                            .collect(Collectors.toList())
            ).status(HttpStatus.OK_200);
        }
    }
}
