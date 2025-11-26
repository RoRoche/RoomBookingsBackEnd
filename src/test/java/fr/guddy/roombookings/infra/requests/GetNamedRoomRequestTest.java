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

final class GetNamedRoomRequestTest {

  @RegisterExtension
  @SuppressWarnings("JTCOP.RuleProhibitStaticFields")
  static final ApiExternalExtension api = new ApiExternalExtension();

  @Test
  void hasNotFound() throws Exception {
    MatcherAssert.assertThat(
      "No room found for given name",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll),
        get("http://localhost:7000/rooms/test_name")::asString
      ).response(),
      new AllOf<>(
        new HasStatus(HttpStatus.NOT_FOUND_404),
        new HasBody("No room found for name 'test_name'")
      )
    );
  }

  @Test
  void isOK() throws Exception {
    MatcherAssert.assertThat(
      "Room found for given name",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll, () ->
          api.rooms().create(new SimpleRoom("test_name", 12))
        ),
        get("http://localhost:7000/rooms/test_name")::asString
      ).response(),
      new AllOf<>(
        new HasStatus(HttpStatus.OK_200),
        new HasBody("{\"name\":\"test_name\",\"capacity\":12}")
      )
    );
  }
}
