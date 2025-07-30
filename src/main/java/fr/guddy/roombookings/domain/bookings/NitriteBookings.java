package fr.guddy.roombookings.domain.bookings;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.booking.NitriteBooking;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.domain.slot.Slot;
import io.vavr.control.Try;
import org.dizitart.no2.*;
import org.dizitart.no2.filters.Filters;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.dizitart.no2.filters.Filters.*;

public final class NitriteBookings implements Bookings {
    private static final String DOCUMENT_KEY_ROOM_NAME = "room_name";
    private static final String DOCUMENT_KEY_USER_ID = "user_id";
    private static final String DOCUMENT_KEY_SLOT_TIMESTAMP_START = "slot_timestamp_start";
    private static final String DOCUMENT_KEY_SLOT_TIMESTAMP_END = "slot_timestamp_end";

    private final NitriteCollection collection;
    private final Rooms rooms;

    public NitriteBookings(final NitriteCollection collection, final Rooms rooms) {
        this.collection = collection;
        this.rooms = rooms;
    }

    public NitriteBookings(final Nitrite database, final Rooms rooms) {
        this(database.getCollection("bookings"), rooms);
    }

    @Override
    public Long create(final Booking booking) {
        return collection.insert(
                new Document(
                        new NitriteBooking(booking).map()
                )
        ).iterator().next().getIdValue();
    }

    @Override
    public List<Booking> bookingsForRoomInSlot(final Room room, final Slot slot) {
        final List<Document> documents = collection.find(
                and(
                        eq(DOCUMENT_KEY_ROOM_NAME, room.name()),
                        or(
                                and(
                                        lte(DOCUMENT_KEY_SLOT_TIMESTAMP_START, slot.timestampEnd()),
                                        gte(DOCUMENT_KEY_SLOT_TIMESTAMP_END, slot.timestampEnd())
                                ),
                                and(
                                        lte(DOCUMENT_KEY_SLOT_TIMESTAMP_START, slot.timestampStart()),
                                        gte(DOCUMENT_KEY_SLOT_TIMESTAMP_END, slot.timestampStart())
                                ),
                                and(
                                        gte(DOCUMENT_KEY_SLOT_TIMESTAMP_START, slot.timestampStart()),
                                        lte(DOCUMENT_KEY_SLOT_TIMESTAMP_END, slot.timestampEnd())
                                )
                        )
                )
        ).toList();
        return documents.stream()
                .map(document ->
                        Try.of(() -> new NitriteBooking(document, rooms)).get()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> bookingsForUserFromStartDate(final String userId, final long timestampStart) {
        final List<Document> documents = collection.find(
                and(
                        eq(DOCUMENT_KEY_USER_ID, userId),
                        or(
                                gte(DOCUMENT_KEY_SLOT_TIMESTAMP_START, timestampStart),
                                gte(DOCUMENT_KEY_SLOT_TIMESTAMP_END, timestampStart)
                        )
                )
        ).toList();
        return documents.stream()
                .map(document ->
                        Try.of(() -> new NitriteBooking(document, rooms)).get()
                )
                .sorted(Comparator.comparingLong(booking -> booking.slot().timestampStart()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isConflicting(final Booking booking) {
        return !bookingsForRoomInSlot(booking.room(), booking.slot()).isEmpty();
    }

    @Override
    public int clearAll() {
        return collection.remove(Filters.ALL).getAffectedCount();
    }

    @Override
    public Optional<Booking> bookingById(final long id) {
        return Optional.ofNullable(
                collection.getById(NitriteId.createId(id))
        ).map(document -> new NitriteBooking(document, rooms));
    }

    @Override
    public boolean delete(final Booking booking) {
        return collection.remove(
                collection.getById(NitriteId.createId(booking.id()))
        ).getAffectedCount() == 1;
    }
}
