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
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.infra.params.LongParameter;
import fr.guddy.roombookings.infra.params.Parameter;
import fr.guddy.roombookings.infra.params.QueryParameter;
import fr.guddy.roombookings.infra.params.RequiredParameter;
import io.javalin.http.Context;
import java.util.List;
import org.eclipse.jetty.http.HttpStatus;

/**
 * {@link Request} to get reminders of {@link Bookings} for a user.
 *
 * @since 1.0.0
 */
public final class GetRemindersRequest implements Request {

    /**
     * The collection of {@link Booking}.
     */
    private final Bookings bookings;

    /**
     * The user ID.
     */
    private final String user;

    /**
     * The starting timestamp.
     */
    private final long start;

    public GetRemindersRequest(final Bookings bookings, final String user, final long start) {
        this.bookings = bookings;
        this.user = user;
        this.start = start;
    }

    public GetRemindersRequest(
        final Bookings bookings,
        final Parameter<String> user,
        final Parameter<Long> start
    ) {
        this(bookings, user.value(), start.value());
    }

    public GetRemindersRequest(final Bookings bookings, final Context context) {
        this(
            bookings,
            new RequiredParameter<>(new QueryParameter("user_id", context)),
            new LongParameter(
                new RequiredParameter<>(
                    new QueryParameter("timestamp_start", context)
                )
            )
        );
    }

    @Override
    public void perform(final Context context) {
        final List<Booking> reminders = this.bookings.forUserFromStartDate(
            this.user,
            this.start
        );
        if (reminders.isEmpty()) {
            context.status(HttpStatus.NO_CONTENT_204);
        } else {
            context
                .json(reminders.stream().map(JsonBooking::new).map(JsonBooking::map).toList())
                .status(HttpStatus.OK_200);
        }
    }
}
