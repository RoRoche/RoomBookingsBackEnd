package fr.guddy.roombookings.domain.booking;

import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.domain.slot.Slot;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.dizitart.no2.Document;

import java.util.Map;

public final class NitriteBooking extends Booking.Envelope {
    private static final String DOCUMENT_KEY_ROOM_NAME = "room_name";
    private static final String DOCUMENT_KEY_USER_ID = "user_id";
    private static final String DOCUMENT_KEY_SLOT_TIMESTAMP_START = "slot_timestamp_start";
    private static final String DOCUMENT_KEY_SLOT_TIMESTAMP_END = "slot_timestamp_end";

    public NitriteBooking(final Booking delegate) {
        super(delegate);
    }

    public NitriteBooking(final Long id, final String userId, final Room room, final Slot slot) {
        this(
                new SimpleBooking(id, userId, room, slot)
        );
    }

    public NitriteBooking(final Document document, final Rooms rooms) throws RoomNotFoundException {
        this(
                document.getId().getIdValue(),
                document.get(DOCUMENT_KEY_USER_ID, String.class),
                document.get(DOCUMENT_KEY_ROOM_NAME, String.class),
                document.get(DOCUMENT_KEY_SLOT_TIMESTAMP_START, Long.class),
                document.get(DOCUMENT_KEY_SLOT_TIMESTAMP_END, Long.class),
                rooms
        );
    }

    public NitriteBooking(
            final Long id,
            final String userId,
            final String roomName,
            final long timestampStart,
            final long timestampEnd,
            final Rooms rooms
    ) {
        this(
                id,
                userId,
                rooms.withName(roomName).orElseThrow(() ->
                        new RoomNotFoundException(roomName)
                ),
                new LogicalSlot(timestampStart, timestampEnd)
        );
    }

    @Override
    public Map<String, Object> map() {
        return new MapOf<>(
                new MapEntry<>(DOCUMENT_KEY_USER_ID, userId()),
                new MapEntry<>(DOCUMENT_KEY_ROOM_NAME, room().name()),
                new MapEntry<>(DOCUMENT_KEY_SLOT_TIMESTAMP_START, slot().timestampStart()),
                new MapEntry<>(DOCUMENT_KEY_SLOT_TIMESTAMP_END, slot().timestampEnd())
        );
    }
}
