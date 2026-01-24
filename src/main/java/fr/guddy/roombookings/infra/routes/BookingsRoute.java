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
package fr.guddy.roombookings.infra.routes;

import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.infra.requests.DeleteBookingRequest;
import fr.guddy.roombookings.infra.requests.GetRemindersRequest;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;

/**
 * Routes to expose {@link Bookings}.
 *
 * @since 1.0.0
 */
public final class BookingsRoute implements EndpointGroup {

    /**
     * Collection of {@link fr.guddy.roombookings.domain.booking.Booking}.
     */
    private final Bookings bookings;

    public BookingsRoute(final Bookings bookings) {
        this.bookings = bookings;
    }

    @Override
    public void addEndpoints() {
        ApiBuilder.get(
            (final Context ctx) -> new GetRemindersRequest(this.bookings, ctx).perform(ctx)
        );
        ApiBuilder.delete(
            "/{id}",
            (final Context ctx) -> new DeleteBookingRequest(this.bookings, ctx).perform(ctx)
        );
    }
}
