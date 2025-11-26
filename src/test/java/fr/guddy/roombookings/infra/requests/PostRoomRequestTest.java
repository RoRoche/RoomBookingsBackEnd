package fr.guddy.roombookings.infra.requests;

import static com.mashape.unirest.http.Unirest.post;

import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.matchers.HasBody;
import fr.guddy.roombookings.infra.matchers.HasHeaderWithValue;
import fr.guddy.roombookings.infra.matchers.HasStatus;
import org.cactoos.list.ListOf;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

final class PostRoomRequestTest {

  @RegisterExtension
  @SuppressWarnings("JTCOP.RuleProhibitStaticFields")
  static final ApiExternalExtension api = new ApiExternalExtension();

  @Test
  void isOK() throws Exception {
    MatcherAssert.assertThat(
      "Create room is successful",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll),
        post("http://localhost:7000/rooms").body(
          "{\"name\":\"test_name\",\"capacity\":12}"
        )::asString
      ).response(),
      new AllOf<>(
        new HasStatus(HttpStatus.CREATED_201),
        new HasHeaderWithValue("Location", Matchers.startsWith("/rooms/"))
      )
    );
  }

  @Test
  void isConflict() throws Exception {
    MatcherAssert.assertThat(
      "Is conflicting on name",
      new HttpTestCase.WithFixtures<>(
        new ListOf<>(api.rooms()::clearAll, () ->
          api.rooms().create(new SimpleRoom("test_name", 12))
        ),
        post("http://localhost:7000/rooms").body(
          "{\"name\":\"test_name\",\"capacity\":12}"
        )::asString
      ).response(),
      new AllOf<>(
        new HasStatus(HttpStatus.CONFLICT_409),
        new HasBody("A room named 'test_name' already exists")
      )
    );
  }
}
