package fr.guddy.roombookings.domain.bookings;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.booking.NitriteBooking;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.domain.slot.Slot;
import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;

import java.util.List;
import java.util.stream.Collectors;

import static org.dizitart.no2.filters.Filters.*;

public final class NitriteBookings implements Bookings {
    private static final String DOCUMENT_KEY_ROOM_NAME = "room_name";
    private static final String DOCUMENT_KEY_SLOT_TIMESTAMP_START = "slot_timestamp_start";
    private static final String DOCUMENT_KEY_SLOT_TIMESTAMP_END = "slot_timestamp_end";

    private final NitriteCollection collection;
    private final Rooms rooms;

    public NitriteBookings(final NitriteCollection collection, final Rooms rooms) {
        this.collection = collection;
        this.rooms = rooms;
    }

    @Override
    public void create(final Booking booking) {
        collection.insert(
                new Document(
                        new NitriteBooking(booking).map()
                )
        );
    }

    @Override
    public List<Booking> bookings() {
        return collection.find()
                .toList()
                .stream()
                .map(
                        document -> new NitriteBooking(document, rooms)
                ).collect(Collectors.toList());
    }

    @Override
    public List<Booking> bookingsForRoomInSlot(final Room room, final Slot slot) {
        final List<Document> documents = collection.find(
                and(
                        eq(DOCUMENT_KEY_ROOM_NAME, room.name()),
                        or(
                                and(
                                        lte(DOCUMENT_KEY_SLOT_TIMESTAMP_START, slot.end().getMillis()),
                                        gte(DOCUMENT_KEY_SLOT_TIMESTAMP_END, slot.end().getMillis())
                                ),
                                and(
                                        lte(DOCUMENT_KEY_SLOT_TIMESTAMP_START, slot.start().getMillis()),
                                        gte(DOCUMENT_KEY_SLOT_TIMESTAMP_END, slot.start().getMillis())
                                )
                        )
                )
        ).toList();
        return documents.stream()
                .map(document ->
                        new NitriteBooking(document, rooms)
                )
                .collect(Collectors.toList());
    }
}
