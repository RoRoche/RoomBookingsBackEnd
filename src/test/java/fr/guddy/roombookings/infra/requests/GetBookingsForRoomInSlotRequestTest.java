package fr.guddy.roombookings.infra.requests;

import static com.mashape.unirest.http.Unirest.get;

import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.matchers.HasBody;
import fr.guddy.roombookings.infra.matchers.HasHttpBookingMatching;
import fr.guddy.roombookings.infra.matchers.HasStatus;
import org.cactoos.list.ListOf;
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
        new ListOf<>(api.rooms()::clearAll, api.bookings()::clearAll, () ->
          api.rooms().create(new SimpleRoom("test_name", 12))
        ),
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
        (id) ->
          () ->
            get("http://localhost:%d/rooms/test_name/bookings".formatted(api.port().value()))
              .queryString("timestamp_start", 1764352800)
              .queryString(
                "timestamp_end",
                Instant.ofEpochSecond(1764352800)
                    .plus(Duration.standardHours(1).getMillis())
                    .getMillis() /
                  1000
              )
              .asString()
      ),
      new HasHttpBookingMatching((id) ->
        new AllOf<>(
          new HasStatus(HttpStatus.OK_200),
          new HasBody(
            String.format(
              "[{\"id\":%d,\"user_id\":\"test_user_id\",\"room\":{\"name\":\"test_name\",\"capacity\":12},\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}]",
              id,
              1764352800,
              Instant.ofEpochSecond(1764352800)
                  .plus(Duration.standardHours(1).getMillis())
                  .getMillis() /
                1000
            )
          )
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
