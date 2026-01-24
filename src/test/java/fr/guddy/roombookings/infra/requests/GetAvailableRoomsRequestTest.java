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
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.fixtures.CreateSimpleBooking;
import fr.guddy.roombookings.infra.fixtures.CreateSimpleRoom;
import fr.guddy.roombookings.infra.matchers.HasBody;
import fr.guddy.roombookings.infra.matchers.HasStatus;
import org.cactoos.list.ListOf;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.AllOf;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

/**
 * Tests on the {@link GetAvailableRoomsRequest}.
 *
 * @since 1.0.0
 */
final class GetAvailableRoomsRequestTest {

    /**
     * The API.
     */
    @RegisterExtension
    @SuppressWarnings("JTCOP.RuleProhibitStaticFields")
    static final ApiExternalExtension API = new ApiExternalExtension();

    @Test
    void hasNoContent() {
        MatcherAssert.assertThat(
            "No available room",
            new HttpTestCase.WithFixtures<>(
                new ListOf<>(
                    GetAvailableRoomsRequestTest.API.rooms()::clearAll,
                    GetAvailableRoomsRequestTest.API.bookings()::clearAll
                ),
                Unirest.get(
                    "http://localhost:%d/rooms".formatted(
                        GetAvailableRoomsRequestTest.API.port().value()
                    )
                )
                    .queryString("capacity", 10)
                    .queryString("timestamp_start", 1_764_352_800)
                    .queryString(
                        "timestamp_end",
                        Instant.ofEpochSecond(1_764_352_800)
                            .plus(Duration.standardHours(1).getMillis())
                            .getMillis() / 1000
                    )::asString
            ),
            new HasStatus(HttpStatus.NO_CONTENT_204)
        );
    }

    @Test
    void isMissingParameter() {
        MatcherAssert.assertThat(
            "A parameter is missing",
            new HttpTestCase.WithFixtures<>(
                new ListOf<>(
                    GetAvailableRoomsRequestTest.API.rooms()::clearAll,
                    GetAvailableRoomsRequestTest.API.bookings()::clearAll
                ),
                Unirest.get(
                    "http://localhost:%d/rooms".formatted(
                        GetAvailableRoomsRequestTest.API.port().value()
                    )
                )
                    .queryString("capacity", 10)
                    .queryString("timestamp_start", 1_764_352_800)::asString
            ),
            new AllOf<>(
                new HasStatus(HttpStatus.BAD_REQUEST_400),
                new HasBody("Parameter named 'timestamp_end' is missing")
            )
        );
    }

    @Test
    void isOkWithAvailableRooms() {
        MatcherAssert.assertThat(
            "A room is available on the given slot",
            new HttpTestCase.WithFixtures<>(
                new ListOf<>(
                    GetAvailableRoomsRequestTest.API.rooms()::clearAll,
                    GetAvailableRoomsRequestTest.API.bookings()::clearAll,
                    new CreateSimpleRoom(GetAvailableRoomsRequestTest.API)
                ),
                Unirest.get(
                    "http://localhost:%d/rooms".formatted(
                        GetAvailableRoomsRequestTest.API.port().value()
                    )
                )
                    .queryString("capacity", 10)
                    .queryString("timestamp_start", 1_764_352_800)
                    .queryString(
                        "timestamp_end",
                        Instant.ofEpochSecond(1_764_352_800)
                            .plus(Duration.standardHours(1).getMillis())
                            .getMillis() / 1000
                    )::asString
            ),
            new AllOf<>(
                new HasStatus(HttpStatus.OK_200),
                new HasBody("[{\"name\":\"test_name\",\"capacity\":12}]")
            )
        );
    }

    @Test
    void isOkWithNoAvailableRooms() {
        MatcherAssert.assertThat(
            "No room is available on the given slot",
            new HttpTestCase.WithFixtures<>(
                new ListOf<>(
                    GetAvailableRoomsRequestTest.API.rooms()::clearAll,
                    GetAvailableRoomsRequestTest.API.bookings()::clearAll,
                    new CreateSimpleRoom(GetAvailableRoomsRequestTest.API),
                    new CreateSimpleBooking(GetAvailableRoomsRequestTest.API)
                ),
                Unirest.get(
                    "http://localhost:%d/rooms".formatted(
                        GetAvailableRoomsRequestTest.API.port().value()
                    )
                )
                    .queryString("capacity", 10)
                    .queryString("timestamp_start", 1_764_352_800)
                    .queryString(
                        "timestamp_end",
                        Instant.ofEpochSecond(1_764_352_800)
                            .plus(Duration.standardHours(1).getMillis())
                            .getMillis() / 1000
                    )::asString
            ),
            new HasStatus(HttpStatus.NO_CONTENT_204)
        );
    }
}
