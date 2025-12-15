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

import static com.mashape.unirest.http.Unirest.get;

import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.bodies.JsonBookingBody;
import fr.guddy.roombookings.infra.fixtures.CreateSimpleRoom;
import fr.guddy.roombookings.infra.matchers.HasBody;
import fr.guddy.roombookings.infra.matchers.HasScalarMatching;
import fr.guddy.roombookings.infra.matchers.HasStatus;
import org.cactoos.list.ListOf;
import org.cactoos.text.FormattedText;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.AllOf;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

final class GetBookingsForRoomInSlotRequestTest {

  @RegisterExtension
  @SuppressWarnings("JTCOP.RuleProhibitStaticFields")
  static final ApiExternalExtension api = new ApiExternalExtension();

  @Test
  void hasNoContent() {
    MatcherAssert.assertThat(
      "No booking for room in slot",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll, api.bookings()::clearAll, new CreateSimpleRoom(api)),
        get("http://localhost:%d/rooms/test_name/bookings".formatted(api.port().value()))
          .queryString("timestamp_start", 1764352800)
          .queryString(
            "timestamp_end",
            Instant.ofEpochSecond(1764352800)
                .plus(Duration.standardHours(1).getMillis())
                .getMillis() /
              1000
          )::asString
      ),
      new HasStatus(HttpStatus.NO_CONTENT_204)
    );
  }

  @Test
  void isOK() {
    MatcherAssert.assertThat(
      "Have booking for room in slot",
      new HttpBookingScalar(
        api,
        1764352800,
        Instant.ofEpochSecond(1764352800).plus(Duration.standardHours(1).getMillis()).getMillis() /
          1000,
        (final Long id) ->
          get("http://localhost:%d/rooms/test_name/bookings".formatted(api.port().value()))
            .queryString("timestamp_start", 1764352800)
            .queryString(
              "timestamp_end",
              Instant.ofEpochSecond(1764352800)
                  .plus(Duration.standardHours(1).getMillis())
                  .getMillis() /
                1000
            )::asString
      ),
      new HasScalarMatching<>((final HttpBooking booking) ->
        new AllOf<>(
          new HasStatus(HttpStatus.OK_200),
          new HasBody(new FormattedText("[%s]", new JsonBookingBody(booking)))
        )
      )
    );
  }

  @Test
  void isRoomNotFound() {
    MatcherAssert.assertThat(
      "No room for given name",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll, api.bookings()::clearAll),
        get("http://localhost:%d/rooms/test_name/bookings".formatted(api.port().value()))
          .queryString("timestamp_start", 1764352800)
          .queryString(
            "timestamp_end",
            Instant.ofEpochSecond(1764352800)
                .plus(Duration.standardHours(1).getMillis())
                .getMillis() /
              1000
          )::asString
      ),
      new AllOf<>(
        new HasStatus(HttpStatus.NOT_FOUND_404),
        new HasBody("No room found for name 'test_name'")
      )
    );
  }
}
