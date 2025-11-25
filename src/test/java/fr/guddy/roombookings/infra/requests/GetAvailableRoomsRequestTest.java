package fr.guddy.roombookings.infra.requests;

import static com.mashape.unirest.http.Unirest.get;

import fr.guddy.roombookings.domain.booking.SimpleBooking;
import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.matchers.HasBody;
import fr.guddy.roombookings.infra.matchers.HasStatus;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.AllOf;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

final class GetAvailableRoomsRequestTest {

  @RegisterExtension
  @SuppressWarnings("JTCOP.RuleProhibitStaticFields")
  static final ApiExternalExtension api = new ApiExternalExtension();

  @Test
  void hasNoContent() throws Exception {
    MatcherAssert.assertThat(
      "No available room",
      new HttpTestCase.WithFixtures<>(
        get("http://localhost:7000/rooms")
          .queryString("capacity", 10)
          .queryString("timestamp_start", Instant.now().getMillis() / 1000)
          .queryString(
            "timestamp_end",
            Instant.now().plus(Duration.standardHours(1).getMillis()).getMillis() / 1000
          )::asString,
        api.rooms()::clearAll,
        api.bookings()::clearAll
      ).response(),
      new HasStatus(HttpStatus.NO_CONTENT_204)
    );
  }

  @Test
  void isMissingParameter() throws Exception {
    MatcherAssert.assertThat(
      "A parameter is missing",
      new HttpTestCase.WithFixtures<>(
        () ->
          get("http://localhost:7000/rooms")
            .queryString("capacity", 10)
            .queryString("timestamp_start", Instant.now().getMillis() / 1000)
            .asString(),
        api.rooms()::clearAll,
        api.bookings()::clearAll
      ).response(),
      new AllOf<>(
        new HasStatus(HttpStatus.BAD_REQUEST_400),
        new HasBody("Parameter named 'timestamp_end' is missing")
      )
    );
  }

  @Test
  void isOkWithAvailableRooms() throws Exception {
    MatcherAssert.assertThat(
      "A room is available on the given slot",
      new HttpTestCase.WithFixtures<>(
        () ->
          get("http://localhost:7000/rooms")
            .queryString("capacity", 10)
            .queryString("timestamp_start", Instant.now().getMillis() / 1000)
            .queryString(
              "timestamp_end",
              Instant.now().plus(Duration.standardHours(1).getMillis()).getMillis() / 1000
            )
            .asString(),
        api.rooms()::clearAll,
        api.bookings()::clearAll,
        () -> api.rooms().create(new SimpleRoom("test_name", 12))
      ).response(),
      new AllOf<>(
        new HasStatus(HttpStatus.OK_200),
        new HasBody("[{\"name\":\"test_name\",\"capacity\":12}]")
      )
    );
  }

  @Test
  void isOkWithNoAvailableRooms() throws Exception {
    MatcherAssert.assertThat(
      "No room is available on the given slot",
      new HttpTestCase.WithFixtures<>(
        () ->
          get("http://localhost:7000/rooms")
            .queryString("capacity", 10)
            .queryString("timestamp_start", Instant.now().getMillis() / 1000)
            .queryString(
              "timestamp_end",
              Instant.now().plus(Duration.standardHours(1).getMillis()).getMillis() / 1000
            )
            .asString(),
        api.rooms()::clearAll,
        api.bookings()::clearAll,
        () -> api.rooms().create(new SimpleRoom("test_name", 12)),
        () ->
          api
            .bookings()
            .create(
              new SimpleBooking(
                null,
                "test_user_id",
                new SimpleRoom("test_name", 12),
                new LogicalSlot(
                  Instant.now().plus(Duration.standardMinutes(15).getMillis()).getMillis() / 1000,
                  Instant.now().plus(Duration.standardMinutes(45).getMillis()).getMillis() / 1000
                )
              )
            )
      ).response(),
      new AllOf<>(new HasStatus(HttpStatus.NO_CONTENT_204))
    );
  }
}
