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
package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.domain.slot.Slot;
import fr.guddy.roombookings.infra.params.IntegerParameter;
import fr.guddy.roombookings.infra.params.LongParameter;
import fr.guddy.roombookings.infra.params.Parameter;
import fr.guddy.roombookings.infra.params.QueryParameter;
import fr.guddy.roombookings.infra.params.RequiredParameter;
import io.javalin.http.Context;
import java.util.List;
import org.eclipse.jetty.http.HttpStatus;

/**
 * {@link Request} to get {@link Rooms} available
 * on a given {@link Slot} and for a given capacity.
 *
 * @since 1.0.0
 */
public final class GetAvailableRoomsRequest implements Request {

    /**
     * The collection of {@link Room}.
     */
    private final Rooms rooms;

    /**
     * The collection of {@link Booking}.
     */
    private final Bookings bookings;

    /**
     * The expected capacity.
     */
    private final int capacity;

    /**
     * The desired {@link Slot}.
     */
    private final Slot slot;

    /**
     * Secondary constructor.
     *
     * @param rooms The collection of {@link Room}.
     * @param bookings The collection of {@link Booking}.
     * @param capacity The targeted capacity.
     * @param slot The targeted {@link Slot}.
     * @checkstyle ParameterNumberCheck (10 lines)
     */
    public GetAvailableRoomsRequest(
        final Rooms rooms,
        final Bookings bookings,
        final int capacity,
        final Slot slot
    ) {
        this.rooms = rooms;
        this.bookings = bookings;
        this.capacity = capacity;
        this.slot = slot;
    }

    /**
     * Secondary constructor.
     *
     * @param rooms The collection of {@link Room}.
     * @param bookings The collection of {@link Booking}.
     * @param capacity The targeted capacity.
     * @param start The starting timestamp.
     * @param end The ending timestamp.
     * @checkstyle ParameterNumberCheck (10 lines)
     */
    public GetAvailableRoomsRequest(
        final Rooms rooms,
        final Bookings bookings,
        final Parameter<Integer> capacity,
        final Parameter<Long> start,
        final Parameter<Long> end
    ) {
        this(
            rooms,
            bookings,
            capacity.value(),
            new LogicalSlot(start.value(), end.value())
        );
    }

    public GetAvailableRoomsRequest(
        final Rooms rooms,
        final Bookings bookings,
        final Context context
    ) {
        this(
            rooms,
            bookings,
            new IntegerParameter(new RequiredParameter<>(new QueryParameter("capacity", context))),
            new LongParameter(
                new RequiredParameter<>(new QueryParameter("timestamp_start", context))
            ),
            new LongParameter(new RequiredParameter<>(new QueryParameter("timestamp_end", context)))
        );
    }

    @Override
    public void perform(final Context context) {
        final List<Room> available = this.rooms
            .withCapacity(this.capacity)
            .stream()
            .filter((final Room room) -> this.bookings.forRoomInSlot(room, this.slot).isEmpty())
            .toList();
        if (available.isEmpty()) {
            context.status(HttpStatus.NO_CONTENT_204);
        } else {
            context
                .json(available.stream().map(JsonRoom::new).map(JsonRoom::map).toList())
                .status(HttpStatus.OK_200);
        }
    }
}
