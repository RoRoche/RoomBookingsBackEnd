package fr.guddy.roombookings.infra.requests;

import static com.mashape.unirest.http.Unirest.get;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.guddy.roombookings.domain.fixtures.ChainedFixtures;
import fr.guddy.roombookings.domain.fixtures.ClearAllRoomsFixture;
import fr.guddy.roombookings.domain.fixtures.CreateRoomFixture;
import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.assertions.WithFixtureAssertion;
import fr.guddy.roombookings.infra.assertions.requests.RequestHasStatusCodeAssertion;
import fr.guddy.roombookings.infra.assertions.requests.RequestWithBodyAssertion;
import fr.guddy.roombookings.infra.matchers.HttpResponseBodyMatcher;
import fr.guddy.roombookings.infra.matchers.HttpResponseStatusMatcher;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

final class GetCapableRoomsRequestTest {

  @RegisterExtension
  static final ApiExternalExtension api = new ApiExternalExtension();

  @Test
  void hasNoContent() {
    new WithFixtureAssertion(
      new ClearAllRoomsFixture(api.rooms()),
      new RequestHasStatusCodeAssertion(
        get("http://localhost:7000/rooms").queryString("capacity", 10).getHttpRequest(),
        HttpStatus.NO_CONTENT_204
      )
    ).check();
  }

  @Test
  void isNotProcessableParameter() throws UnirestException {
    new ClearAllRoomsFixture(api.rooms()).perform();
    final HttpResponse<String> response = get("http://localhost:7000/rooms")
      .queryString("capacity", "test")
      .getHttpRequest()
      .asString();
    MatcherAssert.assertThat(
      response,
      Matchers.allOf(
        new HttpResponseStatusMatcher(HttpStatus.BAD_REQUEST_400),
        new HttpResponseBodyMatcher(
          "Parameter 'capacity' could not be processed, it should be of type Integer"
        )
      )
    );
  }

  @Test
  void isOK() {
    new WithFixtureAssertion(
      new ChainedFixtures(
        new ClearAllRoomsFixture(api.rooms()),
        new CreateRoomFixture(api.rooms(), new SimpleRoom("test_name", 12))
      ),
      new RequestWithBodyAssertion(
        new RequestHasStatusCodeAssertion(
          get("http://localhost:7000/rooms").queryString("capacity", 10).getHttpRequest(),
          HttpStatus.OK_200
        ),
        "[{\"name\":\"test_name\",\"capacity\":12}]"
      )
    ).check();
  }
}
