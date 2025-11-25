package fr.guddy.roombookings.infra.requests;

import static com.mashape.unirest.http.Unirest.delete;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.guddy.roombookings.domain.booking.SimpleBooking;
import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.matchers.HasBody;
import fr.guddy.roombookings.infra.matchers.HasStatus;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.core.AllOf;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

final class DeleteBookingRequestTest {

  @RegisterExtension
  @SuppressWarnings("JTCOP.RuleProhibitStaticFields")
  static final ApiExternalExtension api = new ApiExternalExtension();

  @Test
  void isNotFound() throws Exception {
    assertThat(
      "No booking with id 12 were found",
      new HttpTestCase.WithFixtures<>(
        () -> delete("http://localhost:7000/bookings/12").asString(),
        api.rooms()::clearAll,
        api.bookings()::clearAll
      ).response(),
      new AllOf<>(
        new HasStatus(HttpStatus.NOT_FOUND_404),
        new HasBody("No booking with id 12 were found")
      )
    );
  }

  @Test
  void isOK() throws UnirestException {
    // given
    api.rooms().clearAll();
    api.bookings().clearAll();
    final SimpleRoom room = new SimpleRoom("test_name", 12);
    api.rooms().create(room);
    final long timestampStart = Instant.now().getMillis() / 1000;
    final long timestampEnd =
      Instant.now().plus(Duration.standardHours(1).getMillis()).getMillis() / 1000;
    final long id = api
      .bookings()
      .create(
        new SimpleBooking(null, "test_user_id", room, new LogicalSlot(timestampStart, timestampEnd))
      );

    // when
    final HttpResponse<String> response = delete(
      String.format("http://localhost:7000/bookings/%d", id)
    ).asString();

    // then
    assertThat(
      "Delete booking has body",
      response,
      new HasBody(
        String.format(
          "{\"id\":%d,\"user_id\":\"test_user_id\",\"room\":{\"name\":\"test_name\",\"capacity\":12},\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}",
          id,
          timestampStart,
          timestampEnd
        )
      )
    );
    assertThat(
      "A booking is present in the database",
      api.bookings().byId(id).isPresent(),
      is(false)
    );
  }
}
