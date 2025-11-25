package fr.guddy.roombookings.infra.requests;

import static com.mashape.unirest.http.Unirest.get;

import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.matchers.HasBody;
import fr.guddy.roombookings.infra.matchers.HasStatus;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.AllOf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

final class GetRoomsRequestTest {

  @RegisterExtension
  @SuppressWarnings("JTCOP.RuleProhibitStaticFields")
  static final ApiExternalExtension api = new ApiExternalExtension();

  @Test
  void hasNoContent() throws Exception {
    MatcherAssert.assertThat(
      "No rooms",
      new HttpTestCase.WithFixtures<>(
        get("http://localhost:7000/rooms")::asString,
        api.rooms()::clearAll
      ).response(),
      new HasStatus(HttpStatus.NO_CONTENT_204)
    );
  }

  @Test
  void isOK() throws Exception {
    MatcherAssert.assertThat(
      "Has rooms",
      new HttpTestCase.WithFixtures<>(
        get("http://localhost:7000/rooms")::asString,
        api.rooms()::clearAll,
        () -> api.rooms().create(new SimpleRoom("test_name", 12))
      ).response(),
      new AllOf<>(
        new HasStatus(HttpStatus.OK_200),
        new HasBody("[{\"name\":\"test_name\",\"capacity\":12}]")
      )
    );
  }
}
