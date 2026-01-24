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
import fr.guddy.roombookings.domain.booking.JsonBooking;
import fr.guddy.roombookings.domain.booking.SimpleBooking;
import fr.guddy.roombookings.domain.bookings.BookingNotFoundException;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.bookings.CreateBookingConflictException;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.params.Parameter;
import fr.guddy.roombookings.infra.params.PathParameter;
import fr.guddy.roombookings.infra.params.RequiredParameter;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

/**
 * {@link Request} to create a {@link Booking}.
 *
 * @since 1.0.0
 */
public final class PostBookingRequest implements Request {

    /**
     * The collection of {@link Booking}.
     */
    private final Bookings bookings;

    /**
     * The {@link Booking} to create.
     */
    private final Booking booking;

    public PostBookingRequest(final Bookings bookings, final Booking booking) {
        this.bookings = bookings;
        this.booking = booking;
    }

    public PostBookingRequest(final Rooms rooms, final Bookings bookings, final Context context) {
        this(
            rooms,
            bookings,
            new RequiredParameter<>(new PathParameter("name", context)),
            new JsonBooking(context.body())
        );
    }

    /**
     * Secondary constructor.
     *
     * @param rooms    The collection of {@link Room}.
     * @param bookings The collection of {@link Booking}.
     * @param room     The {@link Room} name.
     * @param booking  The {@link Booking} to create.
     * @checkstyle ParameterNumberCheck (10 lines)
     */
    public PostBookingRequest(
        final Rooms rooms,
        final Bookings bookings,
        final Parameter<String> room,
        final Booking booking
    ) {
        this(
            bookings,
            new SimpleBooking(
                null,
                booking.userId(),
                rooms
                    .withName(room.value())
                    .orElseThrow(() -> new RoomNotFoundException(room.value())),
                booking.slot()
            )
        );
    }

    @Override
    public void perform(final Context context) {
        if (this.bookings.isConflicting(this.booking)) {
            throw new CreateBookingConflictException(this.booking.room().name());
        } else {
            final Long id = this.bookings.create(this.booking);
            context
                .header("location", String.format("/bookings/%d", id))
                .json(
                    new JsonBooking(
                        this.bookings.byId(id).orElseThrow(() -> new BookingNotFoundException(id))
                    ).map()
                )
                .status(HttpStatus.CREATED_201);
        }
    }
}
