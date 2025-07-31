package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.fixtures.ChainedFixtures;
import fr.guddy.roombookings.domain.fixtures.ClearAllRoomsFixture;
import fr.guddy.roombookings.domain.fixtures.CreateRoomFixture;
import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.assertions.WithFixtureAssertion;
import fr.guddy.roombookings.infra.assertions.requests.RequestHasStatusCodeAssertion;
import fr.guddy.roombookings.infra.assertions.requests.RequestWithBodyAssertion;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.mashape.unirest.http.Unirest.get;

final class GetCapableRoomsRequestTest {
    @RegisterExtension
    static ApiExternalExtension api = new ApiExternalExtension();

    @Test
    void testNoContent() {
        new WithFixtureAssertion(
                new ClearAllRoomsFixture(api.rooms()),
                new RequestHasStatusCodeAssertion(
                        get("http://localhost:7000/rooms")
                                .queryString("capacity", 10)
                                .getHttpRequest(),
                        HttpStatus.NO_CONTENT_204
                )
        ).check();
    }

    @Test
    void testNotProcessableParameter() {
        new WithFixtureAssertion(
                new ClearAllRoomsFixture(api.rooms()),
                new RequestWithBodyAssertion(
                        new RequestHasStatusCodeAssertion(
                                get("http://localhost:7000/rooms")
                                        .queryString("capacity", "test")
                                        .getHttpRequest(),
                                HttpStatus.BAD_REQUEST_400
                        ),
                        "Parameter 'capacity' could not be processed, it should be of type Integer"
                )
        ).check();
    }

    @Test
    void testOK() {
        new WithFixtureAssertion(
                new ChainedFixtures(
                        new ClearAllRoomsFixture(api.rooms()),
                        new CreateRoomFixture(
                                api.rooms(),
                                new SimpleRoom("test_name", 12)
                        )
                ),
                new RequestWithBodyAssertion(
                        new RequestHasStatusCodeAssertion(
                                get("http://localhost:7000/rooms")
                                        .queryString("capacity", 10)
                                        .getHttpRequest(),
                                HttpStatus.OK_200
                        ),
                        "[{\"name\":\"test_name\",\"capacity\":12}]"
                )
        ).check();
    }
}