package fr.guddy.roombookings.infra.requests;

import static com.mashape.unirest.http.Unirest.get;

import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.matchers.HasBody;
import fr.guddy.roombookings.infra.matchers.HasStatus;
import org.cactoos.list.ListOf;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.AllOf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

final class GetCapableRoomsRequestTest {

  @RegisterExtension
  @SuppressWarnings("JTCOP.RuleProhibitStaticFields")
  static final ApiExternalExtension api = new ApiExternalExtension();

  @Test
  void hasNoContent() {
    MatcherAssert.assertThat(
      "Rooms has no content",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll),
        get("http://localhost:%d/rooms".formatted(api.port().value())).queryString(
          "capacity",
          10
        )::asString
      ),
      new HasStatus(HttpStatus.NO_CONTENT_204)
    );
  }

  @Test
  void isNotProcessableParameter() throws Exception {
    MatcherAssert.assertThat(
      "Parameter is not processable",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll),
        get("http://localhost:%d/rooms".formatted(api.port().value())).queryString(
          "capacity",
          "test"
        )::asString
      ),
      new AllOf<>(
        new HasStatus(HttpStatus.BAD_REQUEST_400),
        new HasBody("Parameter 'capacity' could not be processed, it should be of type Integer")
      )
    );
  }

  @Test
  void isOK() {
    MatcherAssert.assertThat(
      "Has capable rooms",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll, () ->
          api.rooms().create(new SimpleRoom("test_name", 12))
        ),
        get("http://localhost:%d/rooms".formatted(api.port().value())).queryString(
          "capacity",
          10
        )::asString
      ),
      new AllOf<>(
        new HasStatus(HttpStatus.OK_200),
        new HasBody("[{\"name\":\"test_name\",\"capacity\":12}]")
      )
    );
  }
}
