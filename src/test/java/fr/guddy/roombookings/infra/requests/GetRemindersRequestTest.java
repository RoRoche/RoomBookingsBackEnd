package fr.guddy.roombookings.infra.requests;

import static com.mashape.unirest.http.Unirest.get;

import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.matchers.HasBody;
import fr.guddy.roombookings.infra.matchers.HasScalarMatching;
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
  void isMissingParameter() {
    MatcherAssert.assertThat(
      "Is missing parameter",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll, api.bookings()::clearAll),
        get("http://localhost:%d/bookings".formatted(api.port().value())).getHttpRequest()::asString
      ),
      new AllOf<>(
        new HasStatus(HttpStatus.BAD_REQUEST_400),
        new HasBody("Parameter named 'user_id' is missing")
      )
    );
  }

  @Test
  void hasNoContent() {
    MatcherAssert.assertThat(
      "Has no content",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll, api.bookings()::clearAll),
        get("http://localhost:%d/bookings".formatted(api.port().value())).queryString(
          "user_id",
          "test_user_id"
        )::asString
      ),
      new HasStatus(HttpStatus.NO_CONTENT_204)
    );
  }

  @Test
  void hasContent() {
    MatcherAssert.assertThat(
      "Has reminders",
      new RemindersScalar(api, 1764352800),
      new HasScalarMatching<>((reminders) ->
        new AllOf<>(
          new HasStatus(HttpStatus.OK_200),
          new HasBody(
            String.format(
              "[" +
                "{\"id\":%d,\"user_id\":\"test_user_id\",\"room\":{\"name\":\"test_name\",\"capacity\":12},\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}," +
                "{\"id\":%d,\"user_id\":\"test_user_id\",\"room\":{\"name\":\"test_name\",\"capacity\":12},\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}" +
                "]",
              reminders.idMinus15ToPlus15m(),
              Instant.ofEpochSecond(1764352800)
                  .minus(Duration.standardMinutes(15).getMillis())
                  .getMillis() /
                1000,
              Instant.ofEpochSecond(1764352800)
                  .plus(Duration.standardMinutes(15).getMillis())
                  .getMillis() /
                1000,
              reminders.idPlus15To45m(),
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
      )
    );
  }
}
