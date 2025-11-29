package fr.guddy.roombookings.infra.requests;

import static com.mashape.unirest.http.Unirest.get;

import fr.guddy.roombookings.domain.booking.SimpleBooking;
import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
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
    // given
    final SimpleRoom room = new SimpleRoom("test_name", 12);
    api.rooms().clearAll();
    api.bookings().clearAll();
    api.rooms().create(room);
    final long id = api
      .bookings()
      .create(
        new SimpleBooking(
          null,
          "test_user_id",
          room,
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
      );

    // then
    MatcherAssert.assertThat(
      "Have booking for room in slot",
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
          .asString(),
      new AllOf<>(
        new HasStatus(HttpStatus.OK_200),
        new HasBody(
          String.format(
            "[{\"id\":%d,\"user_id\":\"test_user_id\",\"room\":{\"name\":\"test_name\",\"capacity\":12},\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}]",
            id,
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
