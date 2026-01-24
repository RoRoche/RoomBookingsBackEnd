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
package fr.guddy.roombookings.infra.bodies;

import org.cactoos.text.FormattedText;
import org.cactoos.text.TextEnvelope;
import org.joda.time.Duration;
import org.joda.time.Instant;

/**
 * Custom body to post a {@link fr.guddy.roombookings.domain.booking.JsonBooking}.
 *
 * @since 1.0.0
 */
public final class PostJsonBookingBody extends TextEnvelope {

    public PostJsonBookingBody() {
        super(
            new FormattedText(
                "{\"user_id\":\"test_user_id\",\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}",
                1_764_352_800,
                Instant.ofEpochSecond(1_764_352_800)
                    .plus(Duration.standardHours(1).getMillis())
                    .getMillis() / 1000
            )
        );
    }
}
