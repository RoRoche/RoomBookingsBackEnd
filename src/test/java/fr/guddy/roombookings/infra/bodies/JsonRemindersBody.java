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

import fr.guddy.roombookings.infra.requests.Reminders;
import javax.json.Json;
import org.cactoos.text.TextEnvelope;
import org.cactoos.text.TextOf;
import org.joda.time.Duration;
import org.joda.time.Instant;

/**
 * Custom body to post {@link Reminders}.
 *
 * @since 1.0.0
 */
public final class JsonRemindersBody extends TextEnvelope {

    public JsonRemindersBody(final Reminders reminders) {
        super(
            new TextOf(
                Json.createArrayBuilder()
                    .add(
                        Json.createObjectBuilder()
                            .add("id", reminders.idMin15ToPlus15())
                            .add("user_id", "test_user_id")
                            .add(
                                "room",
                                Json.createObjectBuilder()
                                    .add("name", "test_name")
                                    .add("capacity", 12)
                            )
                            .add(
                                "slot",
                                Json.createObjectBuilder()
                                    .add(
                                        "timestamp_start",
                                        Instant.ofEpochSecond(1_764_352_800)
                                            .minus(Duration.standardMinutes(15).getMillis())
                                            .getMillis() / 1000
                                    )
                                    .add(
                                        "timestamp_end",
                                        Instant.ofEpochSecond(1_764_352_800)
                                            .plus(Duration.standardMinutes(15).getMillis())
                                            .getMillis() / 1000
                                    )
                            )
                            .build()
                    )
                    .add(
                        Json.createObjectBuilder()
                            .add("id", reminders.idPlus15To45())
                            .add("user_id", "test_user_id")
                            .add(
                                "room",
                                Json.createObjectBuilder()
                                    .add("name", "test_name")
                                    .add("capacity", 12)
                            )
                            .add(
                                "slot",
                                Json.createObjectBuilder()
                                    .add(
                                        "timestamp_start",
                                        Instant.ofEpochSecond(1_764_352_800)
                                            .plus(Duration.standardMinutes(15).getMillis())
                                            .getMillis() / 1000
                                    )
                                    .add(
                                        "timestamp_end",
                                        Instant.ofEpochSecond(1_764_352_800)
                                            .plus(Duration.standardMinutes(45).getMillis())
                                            .getMillis() / 1000
                                    )
                            )
                            .build()
                    )
                    .build()
                    .toString()
            )
        );
    }
}
