package fr.guddy.roombookings.infra.requests;

import static com.mashape.unirest.http.Unirest.get;

import com.mashape.unirest.http.exceptions.UnirestException;
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

final class GetRemindersRequestTest {

  @RegisterExtension
  @SuppressWarnings("JTCOP.RuleProhibitStaticFields")
  static final ApiExternalExtension api = new ApiExternalExtension();

  @Test
  void isMissingParameter() throws Exception {
    MatcherAssert.assertThat(
      "Is missing parameter",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll, api.bookings()::clearAll),
        get("http://localhost:%d/bookings".formatted(api.port().value())).getHttpRequest()::asString
      ).response(),
      new AllOf<>(
        new HasStatus(HttpStatus.BAD_REQUEST_400),
        new HasBody("Parameter named 'user_id' is missing")
      )
    );
  }

  @Test
  void hasNoContent() throws Exception {
    MatcherAssert.assertThat(
      "Has no content",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll, api.bookings()::clearAll),
        get("http://localhost:%d/bookings".formatted(api.port().value())).queryString(
          "user_id",
          "test@test.com"
        )::asString
      ).response(),
      new HasStatus(HttpStatus.NO_CONTENT_204)
    );
  }

  @Test
  void hasContent() throws UnirestException {
    // given
    final long nowMinus45m =
      Instant.now().minus(Duration.standardMinutes(45).getMillis()).getMillis() / 1000;
    final long nowMinus15m =
      Instant.now().minus(Duration.standardMinutes(15).getMillis()).getMillis() / 1000;
    final long nowPlus15m =
      Instant.now().plus(Duration.standardMinutes(15).getMillis()).getMillis() / 1000;
    final long nowPlus45m =
      Instant.now().plus(Duration.standardMinutes(45).getMillis()).getMillis() / 1000;
    final SimpleRoom room = new SimpleRoom("test_name", 12);
    api.rooms().clearAll();
    api.bookings().clearAll();
    api.rooms().create(room);
    api
      .bookings()
      .create(
        new SimpleBooking(null, "test@test.com", room, new LogicalSlot(nowMinus45m, nowMinus15m))
      );
    final long idMinus15ToPlus15m = api
      .bookings()
      .create(
        new SimpleBooking(null, "test@test.com", room, new LogicalSlot(nowMinus15m, nowPlus15m))
      );
    final long id15To45m = api
      .bookings()
      .create(
        new SimpleBooking(null, "test@test.com", room, new LogicalSlot(nowPlus15m, nowPlus45m))
      );

    // then
    MatcherAssert.assertThat(
      "Has reminders",
      get("http://localhost:%d/bookings".formatted(api.port().value()))
        .queryString("user_id", "test@test.com")
        .asString(),
      new AllOf<>(
        new HasStatus(HttpStatus.OK_200),
        new HasBody(
          String.format(
            "[" +
              "{\"id\":%d,\"user_id\":\"test@test.com\",\"room\":{\"name\":\"test_name\",\"capacity\":12},\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}," +
              "{\"id\":%d,\"user_id\":\"test@test.com\",\"room\":{\"name\":\"test_name\",\"capacity\":12},\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}" +
              "]",
            idMinus15ToPlus15m,
            nowMinus15m,
            nowPlus15m,
            id15To45m,
            nowPlus15m,
            nowPlus45m
          )
        )
      )
    );
  }
}
