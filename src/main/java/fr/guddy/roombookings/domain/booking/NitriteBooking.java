package fr.guddy.roombookings.domain.booking;

import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.domain.slot.Slot;
import org.dizitart.no2.Document;

import java.util.HashMap;
import java.util.Map;

public final class NitriteBooking implements Booking {
    private static final String DOCUMENT_KEY_ROOM_NAME = "room_name";
    private static final String DOCUMENT_KEY_USER_ID = "user_id";
    private static final String DOCUMENT_KEY_SLOT_TIMESTAMP_START = "slot_timestamp_start";
    private static final String DOCUMENT_KEY_SLOT_TIMESTAMP_END = "slot_timestamp_end";

    private final Booking delegate;

    public NitriteBooking(final Booking delegate) {
        this.delegate = delegate;
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
                rooms.namedRoom(roomName).orElseThrow(() ->
                        new RoomNotFoundException(roomName)
                ),
                new LogicalSlot(timestampStart, timestampEnd)
        );
    }

    @Override
    public Long id() {
        return delegate.id();
    }

    @Override
    public String userId() {
        return delegate.userId();
    }

    @Override
    public Room room() {
        return delegate.room();
    }

    @Override
    public Slot slot() {
        return delegate.slot();
    }

    @Override
    public Map<String, Object> map() {
        final Map<String, Object> map = new HashMap<>();
        map.put(DOCUMENT_KEY_USER_ID, userId());
        map.put(DOCUMENT_KEY_ROOM_NAME, room().name());
        map.put(DOCUMENT_KEY_SLOT_TIMESTAMP_START, slot().timestampStart());
        map.put(DOCUMENT_KEY_SLOT_TIMESTAMP_END, slot().timestampEnd());
        return map;
    }
}
