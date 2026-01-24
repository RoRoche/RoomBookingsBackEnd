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
package fr.guddy.roombookings.domain.booking;

import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.domain.slot.Slot;
import java.util.LinkedHashMap;
import java.util.Map;
import org.dizitart.no2.collection.Document;

/**
 * {@link Booking} implementation using {@link org.dizitart.no2.Nitrite}.
 *
 * @since 1.0.0
 */
public final class NitriteBooking implements Booking {

    /**
     * The {@link Room} name.
     */
    private static final String ROOM_NAME = "room_name";

    /**
     * The user's ID that booked the {@link Room}.
     */
    private static final String USER_ID = "user_id";

    /**
     * The starting timestamp of the {@link Booking}.
     */
    private static final String TIMESTAMP_START = "slot_timestamp_start";

    /**
     * The ending timestamp of the {@link Booking}.
     */
    private static final String TIMESTAMP_END = "slot_timestamp_end";

    /**
     * The wrapped {@link Booking}.
     */
    private final Booking delegate;

    public NitriteBooking(final Booking delegate) {
        this.delegate = delegate;
    }

    /**
     * Secondary constructor.
     *
     * @param id   The booking ID.
     * @param user The user ID.
     * @param room The room name.
     * @param slot The slot.
     * @checkstyle ParameterNumberCheck (3 lines)
     */
    public NitriteBooking(final Long id, final String user, final Room room, final Slot slot) {
        this(new SimpleBooking(id, user, room, slot));
    }

    public NitriteBooking(final Document document, final Rooms rooms) throws RoomNotFoundException {
        this(
            document.getId().getIdValue(),
            document.get(NitriteBooking.USER_ID, String.class),
            document.get(NitriteBooking.ROOM_NAME, String.class),
            document.get(NitriteBooking.TIMESTAMP_START, Long.class),
            document.get(NitriteBooking.TIMESTAMP_END, Long.class),
            rooms
        );
    }

    /**
     * Secondary constructor.
     *
     * @param id    The booking ID.
     * @param user  The user ID.
     * @param room  The room name.
     * @param start The starting timestamp.
     * @param end   The ending timestamp.
     * @param rooms The collection of rooms.
     * @checkstyle ParameterNumberCheck (20 lines)
     */
    public NitriteBooking(
        final Long id,
        final String user,
        final String room,
        final long start,
        final long end,
        final Rooms rooms
    ) {
        this(
            id,
            user,
            rooms.withName(room).orElseThrow(() -> new RoomNotFoundException(room)),
            new LogicalSlot(start, end)
        );
    }

    @Override
    public Long identifier() {
        return this.delegate.identifier();
    }

    @Override
    public String userId() {
        return this.delegate.userId();
    }

    @Override
    public Room room() {
        return this.delegate.room();
    }

    @Override
    public Slot slot() {
        return this.delegate.slot();
    }

    @Override
    public Map<String, Object> map() {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(NitriteBooking.USER_ID, this.userId());
        map.put(NitriteBooking.ROOM_NAME, this.room().name());
        map.put(NitriteBooking.TIMESTAMP_START, this.slot().timestampStart());
        map.put(NitriteBooking.TIMESTAMP_END, this.slot().timestampEnd());
        return map;
    }
}
