package fr.guddy.roombookings.infra.requests;

import static com.mashape.unirest.http.Unirest.delete;
import static org.hamcrest.MatcherAssert.assertThat;

import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.matchers.HasBody;
import fr.guddy.roombookings.infra.matchers.HasStatus;
import fr.guddy.roombookings.infra.matchers.IsValidBookingDeletion;
import org.cactoos.list.ListOf;
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
  void isNotFound() {
    assertThat(
      "No booking with id 12 were found",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll, api.bookings()::clearAll),
        delete("http://localhost:%d/bookings/12".formatted(api.port().value()))::asString
      ),
      new AllOf<>(
        new HasStatus(HttpStatus.NOT_FOUND_404),
        new HasBody("No booking with id 12 were found")
      )
    );
  }

  @Test
  void isOK() {
    assertThat(
      "Delete booking has body and booking is not in database",
      new HttpBookingScalar(
        api,
        1764352800,
        Instant.ofEpochSecond(1764352800).plus(Duration.standardHours(1).getMillis()).getMillis() /
          1000,
        (id) ->
          delete("http://localhost:%d/bookings/%d".formatted(api.port().value(), id))::asString
      ),
      new IsValidBookingDeletion(api, (id) ->
        String.format(
          "{\"id\":%d,\"user_id\":\"test_user_id\",\"room\":{\"name\":\"test_name\",\"capacity\":12},\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}",
          id,
          1764352800,
          Instant.ofEpochSecond(1764352800)
              .plus(Duration.standardHours(1).getMillis())
              .getMillis() /
            1000
        )
      )
    );
  }
}
