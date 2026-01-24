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

import fr.guddy.roombookings.domain.booking.SimpleBooking;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.domain.slot.Slot;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import org.cactoos.Func;
import org.cactoos.Scalar;

/**
 * {@link Scalar} to provide {@link HttpBooking}.
 *
 * @since 1.0.0
 */
public final class HttpBookingScalar implements Scalar<HttpBooking> {

    /**
     * The API.
     */
    private final ApiExternalExtension api;

    /**
     * The starting timestamp.
     */
    private final long start;

    /**
     * The ending timestamp.
     */
    private final long end;

    /**
     * The {@link Func} to call the {@link HttpTestCase} with the generated ID.
     */
    private final Func<Long, HttpTestCase<String>> callback;

    /**
     * Primary constructor.
     *
     * @param api The API.
     * @param start The starting timestamp.
     * @param end The ending timestamp.
     * @param callback The {@link Func} to call back with the generated ID.
     * @checkstyle ParameterNumberCheck (10 lines)
     */
    public HttpBookingScalar(
        final ApiExternalExtension api,
        final long start,
        final long end,
        final Func<Long, HttpTestCase<String>> callback
    ) {
        this.api = api;
        this.start = start;
        this.end = end;
        this.callback = callback;
    }

    @Override
    public HttpBooking value() throws Exception {
        this.api.rooms().clearAll();
        this.api.bookings().clearAll();
        final Room room = new SimpleRoom("test_name", 12);
        this.api.rooms().create(room);
        final Slot slot = new LogicalSlot(this.start, this.end);
        final long id = this.api.bookings().create(
            new SimpleBooking(null, "test_user_id", room, slot)
        );
        return new HttpBooking(id, "test_user_id", room, slot, this.callback.apply(id));
    }
}
