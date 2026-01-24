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

import com.mashape.unirest.http.Unirest;
import fr.guddy.roombookings.domain.booking.SimpleBooking;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import org.cactoos.Scalar;
import org.joda.time.Duration;
import org.joda.time.Instant;

/**
 * {@link Scalar} providing {@link Reminders}.
 *
 * @since 1.0.0
 */
public final class RemindersScalar implements Scalar<Reminders> {

    /**
     * The API.
     */
    private final ApiExternalExtension api;

    /**
     * The base timestamp.
     */
    private final long timestamp;

    public RemindersScalar(final ApiExternalExtension api, final long timestamp) {
        this.api = api;
        this.timestamp = timestamp;
    }

    @Override
    public Reminders value() throws Exception {
        this.api.rooms().clearAll();
        this.api.bookings().clearAll();
        final Room room = new SimpleRoom("test_name", 12);
        this.api.rooms().create(room);
        final long minusfifteen =
            Instant.ofEpochSecond(this.timestamp)
                .minus(Duration.standardMinutes(15).getMillis())
                .getMillis() / 1000;
        final long plusfifteen =
            Instant.ofEpochSecond(this.timestamp)
                .plus(Duration.standardMinutes(15).getMillis())
                .getMillis() / 1000;
        final long plsfrtfve =
            Instant.ofEpochSecond(this.timestamp)
                .plus(Duration.standardMinutes(45).getMillis())
                .getMillis() / 1000;
        this.api.bookings().create(
            new SimpleBooking(
                null,
                "test_user_id",
                room,
                new LogicalSlot(
                    Instant.ofEpochSecond(this.timestamp)
                        .minus(Duration.standardMinutes(45).getMillis())
                        .getMillis() / 1000,
                    minusfifteen
                )
            )
        );
        return new Reminders(
            Unirest.get(
                "http://localhost:%d/bookings".formatted(this.api.port().value())
            ).queryString(
                "user_id",
                "test_user_id"
            ).queryString("timestamp_start", this.timestamp)::asString,
            this.api.bookings().create(
                new SimpleBooking(
                    null,
                    "test_user_id",
                    room,
                    new LogicalSlot(minusfifteen, plusfifteen)
                )
            ),
            this.api.bookings().create(
                new SimpleBooking(
                    null,
                    "test_user_id",
                    room,
                    new LogicalSlot(plusfifteen, plsfrtfve)
                )
            ),
            minusfifteen,
            plusfifteen,
            plsfrtfve
        );
    }
}
