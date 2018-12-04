package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.fixtures.ChainedFixtures;
import fr.guddy.roombookings.domain.fixtures.ClearAllRoomsFixture;
import fr.guddy.roombookings.domain.fixtures.CreateRoomFixture;
import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.infra.ApiExternalResource;
import fr.guddy.roombookings.infra.assertions.WithFixtureAssertion;
import fr.guddy.roombookings.infra.assertions.requests.RequestHasStatusCodeAssertion;
import fr.guddy.roombookings.infra.assertions.requests.RequestWithBodyAssertion;
import fr.guddy.roombookings.infra.assertions.requests.RequestWithLocationHeaderAssertion;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.ClassRule;
import org.junit.Test;

import static com.mashape.unirest.http.Unirest.post;

public final class PostRoomRequestTest {
    @ClassRule
    public static final ApiExternalResource api = new ApiExternalResource();

    @Test
    public void testOK() {
        new WithFixtureAssertion(
                new ClearAllRoomsFixture(api.rooms()),
                new RequestWithLocationHeaderAssertion(
                        new RequestHasStatusCodeAssertion(
                                post("http://localhost:7000/rooms")
                                        .body("{\"name\":\"test_name\",\"capacity\":12}")
                                        .getHttpRequest(),
                                HttpStatus.CREATED_201
                        ),
                        "/rooms/"
                )
        ).check();
    }

    @Test
    public void testConflict() {
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
                                post("http://localhost:7000/rooms")
                                        .body("{\"name\":\"test_name\",\"capacity\":12}")
                                        .getHttpRequest(),
                                HttpStatus.CONFLICT_409
                        ),
                        "A room named 'test_name' already exists"
                )
        ).check();
    }
}