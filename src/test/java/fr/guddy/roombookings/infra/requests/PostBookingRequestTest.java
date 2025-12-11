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

import static com.mashape.unirest.http.Unirest.post;

import fr.guddy.roombookings.domain.booking.SimpleBooking;
import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.fixtures.CreateSimpleRoom;
import fr.guddy.roombookings.infra.matchers.HasBody;
import fr.guddy.roombookings.infra.matchers.HasBodyContaining;
import fr.guddy.roombookings.infra.matchers.HasHeaderWithValue;
import fr.guddy.roombookings.infra.matchers.HasStatus;
import java.util.Map;
import org.cactoos.list.ListOf;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

final class PostBookingRequestTest {

  @RegisterExtension
  @SuppressWarnings("JTCOP.RuleProhibitStaticFields")
  static final ApiExternalExtension api = new ApiExternalExtension();

  @Test
  void isOK() {
    MatcherAssert.assertThat(
      "Create booking is successful",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll, api.bookings()::clearAll, new CreateSimpleRoom(api)),
        post("http://localhost:%d/rooms/test_name/bookings".formatted(api.port().value())).body(
          String.format(
            "{\"user_id\":\"test_user_id\",\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}",
            1764352800,
            Instant.ofEpochSecond(1764352800)
                .plus(Duration.standardHours(1).getMillis())
                .getMillis() /
              1000
          )
        )::asString
      ),
      new AllOf<>(
        new HasStatus(HttpStatus.CREATED_201),
        new HasBodyContaining(
          Map.of(
            "user_id",
            "test_user_id",
            "room",
            Map.of("name", "test_name", "capacity", 12),
            "slot",
            Map.of(
              "timestamp_start",
              1764352800,
              "timestamp_end",
              Instant.ofEpochSecond(1764352800)
                  .plus(Duration.standardHours(1).getMillis())
                  .getMillis() /
                1000
            )
          )
        ),
        new HasHeaderWithValue("Location", Matchers.startsWith("/bookings/"))
      )
    );
  }

  @Test
  void isConflict() {
    MatcherAssert.assertThat(
      "Has conflict on room and slot",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(
          api.rooms()::clearAll,
          api.bookings()::clearAll,
          new CreateSimpleRoom(api),
          () ->
            api
              .bookings()
              .create(
                new SimpleBooking(
                  null,
                  "test_user_id",
                  new SimpleRoom("test_name", 12),
                  new LogicalSlot(
                    Instant.ofEpochSecond(1764352800)
                        .plus(Duration.standardMinutes(15).getMillis())
                        .getMillis() /
                      1000,
                    Instant.ofEpochSecond(1764352800)
                        .plus(Duration.standardMinutes(45).getMillis())
                        .getMillis() /
                      1000
                  )
                )
              )
        ),
        post("http://localhost:%d/rooms/test_name/bookings".formatted(api.port().value())).body(
          String.format(
            "{\"user_id\":\"test_user_id\",\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}",
            1764352800,
            Instant.ofEpochSecond(1764352800)
                .plus(Duration.standardHours(1).getMillis())
                .getMillis() /
              1000
          )
        )::asString
      ),
      new AllOf<>(
        new HasStatus(HttpStatus.CONFLICT_409),
        new HasBody("Room named 'test_name' already booked on this slot")
      )
    );
  }

  @Test
  void isRoomNotFound() {
    MatcherAssert.assertThat(
      "Room is not found for name",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll, api.bookings()::clearAll),
        post("http://localhost:%d/rooms/test_name/bookings".formatted(api.port().value())).body(
          String.format(
            "{\"user_id\":\"test_user_id\",\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}",
            1764352800,
            Instant.ofEpochSecond(1764352800)
                .plus(Duration.standardHours(1).getMillis())
                .getMillis() /
              1000
          )
        )::asString
      ),
      new AllOf<>(
        new HasStatus(HttpStatus.NOT_FOUND_404),
        new HasBody("No room found for name 'test_name'")
      )
    );
  }
}
