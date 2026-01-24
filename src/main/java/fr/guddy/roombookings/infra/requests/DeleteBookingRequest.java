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
import fr.guddy.roombookings.domain.bookings.BookingNotDeletedException;
import fr.guddy.roombookings.domain.bookings.BookingNotFoundException;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.infra.params.LongParameter;
import fr.guddy.roombookings.infra.params.PathParameter;
import fr.guddy.roombookings.infra.params.RequiredParameter;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

/**
 * Request to delete a {@link Booking} by ID.
 *
 * @since 1.0.0
 */
public final class DeleteBookingRequest implements Request {

    /**
     * The collection of {@link Booking}.
     */
    private final Bookings bookings;

    /**
     * The ID of the {@link Booking} to delete.
     */
    private final long id;

    public DeleteBookingRequest(final Bookings bookings, final long id) {
        this.bookings = bookings;
        this.id = id;
    }

    public DeleteBookingRequest(final Bookings bookings, final Context context) {
        this(
            bookings,
            new LongParameter(new RequiredParameter<>(new PathParameter("id", context))).value()
        );
    }

    @Override
    public void perform(final Context context) {
        final Booking booking = this.bookings
            .byId(this.id)
            .orElseThrow(() -> new BookingNotFoundException(this.id));
        if (this.bookings.delete(booking)) {
            context.json(new JsonBooking(booking).map()).status(HttpStatus.OK_200);
        } else {
            throw new BookingNotDeletedException(this.id);
        }
    }
}
