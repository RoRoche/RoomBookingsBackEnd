/*
 * MIT License
 *
 * Copyright (c) 2018-2025 Romain Rochegude
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package fr.guddy.roombookings.domain.bookings;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.booking.NitriteBooking;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.domain.slot.Slot;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.cactoos.Scalar;
import org.cactoos.scalar.Unchecked;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.NitriteCollection;
import org.dizitart.no2.collection.NitriteId;
import org.dizitart.no2.filters.Filter;
import org.dizitart.no2.filters.FluentFilter;

/**
 * Concrete implementation of {@link Bookings} based on {@link NitriteCollection}.
 *
 * @since 1.0.0
 */
public final class NitriteBookings implements Bookings {

    /**
     * The document ke for room name.
     */
    private static final String ROOM_NAME = "room_name";

    /**
     * The document ke for user ID.
     */
    private static final String USER_ID = "user_id";

    /**
     * The document ke for slot timestamp start.
     */
    private static final String TIMESTAMP_START = "slot_timestamp_start";

    /**
     * The document ke for slot timestamp end.
     */
    private static final String TIMESTAMP_END = "slot_timestamp_end";

    /**
     * The {@link NitriteCollection} containing rooms.
     */
    private final NitriteCollection collection;

    /**
     * The collection of {@link Rooms}.
     */
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
        return this.collection.insert(Document.createDocument(new NitriteBooking(booking).map()))
            .iterator()
            .next()
            .getIdValue();
    }

    @Override
    public List<Booking> forRoomInSlot(final Room room, final Slot slot) {
        return this.collection.find(
            Filter.and(
                FluentFilter.where(NitriteBookings.ROOM_NAME).eq(room.name()),
                Filter.or(
                    Filter.and(
                        FluentFilter.where(
                            NitriteBookings.TIMESTAMP_START
                        ).lte(slot.timestampEnd()),
                        FluentFilter.where(
                            NitriteBookings.TIMESTAMP_END
                        ).gte(slot.timestampEnd())
                    ),
                    Filter.and(
                        FluentFilter.where(
                            NitriteBookings.TIMESTAMP_START
                        ).lte(slot.timestampStart()),
                        FluentFilter.where(
                            NitriteBookings.TIMESTAMP_END
                        ).gte(slot.timestampStart())
                    ),
                    Filter.and(
                        FluentFilter.where(
                            NitriteBookings.TIMESTAMP_START
                        ).gte(slot.timestampStart()),
                        FluentFilter.where(
                            NitriteBookings.TIMESTAMP_END
                        ).lte(slot.timestampEnd())
                    )
                )
            )
            )
            .toList()
            .stream()
            .map(
                (final Document document) ->
                    new Unchecked<>(
                        (Scalar<Booking>) () -> new NitriteBooking(document, this.rooms)
                    ).value()
            )
            .toList();
    }

    @Override
    public List<Booking> forUserFromStartDate(final String user, final long start) {
        return this.collection.find(
            Filter.and(
                FluentFilter.where(NitriteBookings.USER_ID).eq(user),
                Filter.or(
                    FluentFilter.where(NitriteBookings.TIMESTAMP_START).gte(start),
                    FluentFilter.where(NitriteBookings.TIMESTAMP_END).gte(start)
                )
            )
            )
            .toList()
            .stream()
            .map(
                (final Document document) ->
                    new Unchecked<>(
                        (Scalar<Booking>) () -> new NitriteBooking(document, this.rooms)
                    ).value()
            )
            .sorted(
                Comparator.comparingLong((final Booking booking) -> booking.slot().timestampStart())
            ).toList();
    }

    @Override
    public boolean isConflicting(final Booking booking) {
        return !this.forRoomInSlot(booking.room(), booking.slot()).isEmpty();
    }

    @Override
    public int clearAll() {
        return this.collection.remove(Filter.ALL).getAffectedCount();
    }

    @Override
    public Optional<Booking> byId(final long id) {
        return Optional.ofNullable(this.collection.getById(NitriteId.createId(id))).map(
            (final Document document) -> new NitriteBooking(document, this.rooms)
        );
    }

    @Override
    public boolean delete(final Booking booking) {
        return this.collection.remove(
            this.collection.getById(NitriteId.createId(booking.identifier()))
        ).getAffectedCount() == 1;
    }
}
