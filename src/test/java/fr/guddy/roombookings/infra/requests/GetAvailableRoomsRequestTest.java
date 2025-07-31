package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.booking.SimpleBooking;
import fr.guddy.roombookings.domain.fixtures.*;
import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.assertions.WithFixtureAssertion;
import fr.guddy.roombookings.infra.assertions.requests.RequestHasStatusCodeAssertion;
import fr.guddy.roombookings.infra.assertions.requests.RequestWithBodyAssertion;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.mashape.unirest.http.Unirest.get;

final class GetAvailableRoomsRequestTest {
    @RegisterExtension
    static ApiExternalExtension api = new ApiExternalExtension();

    @Test
    void testNoContent() {
        new WithFixtureAssertion(
                new ChainedFixtures(
                        new ClearAllRoomsFixture(api.rooms()),
                        new ClearAllBookingsFixture(api.bookings())
                ),
                new RequestHasStatusCodeAssertion(
                        get("http://localhost:7000/rooms")
                                .queryString("capacity", 10)
                                .queryString("timestamp_start", Instant.now().getMillis() / 1000)
                                .queryString(
                                        "timestamp_end",
                                        Instant.now().plus(Duration.standardHours(1).getMillis()).getMillis() / 1000
                                ).getHttpRequest(),
                        HttpStatus.NO_CONTENT_204
                )
        ).check();
    }

    @Test
    void testMissingParameter() {
        new WithFixtureAssertion(
                new ChainedFixtures(
                        new ClearAllRoomsFixture(api.rooms()),
                        new ClearAllBookingsFixture(api.bookings())
                ),
                new RequestWithBodyAssertion(
                        new RequestHasStatusCodeAssertion(
                                get("http://localhost:7000/rooms")
                                        .queryString("capacity", 10)
                                        .queryString("timestamp_start", Instant.now().getMillis() / 1000)
                                        .getHttpRequest(),
                                HttpStatus.BAD_REQUEST_400
                        ),
                        "Parameter named 'timestamp_end' is missing"
                )
        ).check();
    }

    @Test
    void testOkWithAvailableRooms() {
        new WithFixtureAssertion(
                new ChainedFixtures(
                        new ClearAllRoomsFixture(api.rooms()),
                        new ClearAllBookingsFixture(api.bookings()),
                        new CreateRoomFixture(
                                api.rooms(),
                                new SimpleRoom("test_name", 12)
                        )
                ),
                new RequestWithBodyAssertion(
                        new RequestHasStatusCodeAssertion(
                                get("http://localhost:7000/rooms")
                                        .queryString("capacity", 10)
                                        .queryString("timestamp_start", Instant.now().getMillis() / 1000)
                                        .queryString(
                                                "timestamp_end",
                                                Instant.now().plus(Duration.standardHours(1).getMillis()).getMillis() / 1000
                                        ).getHttpRequest(),
                                HttpStatus.OK_200
                        ),
                        "[{\"name\":\"test_name\",\"capacity\":12}]"
                )
        ).check();
    }

    @Test
    void testOkWithNoAvailableRooms() {
        new WithFixtureAssertion(
                new ChainedFixtures(
                        new ClearAllRoomsFixture(api.rooms()),
                        new ClearAllBookingsFixture(api.bookings()),
                        new CreateRoomFixture(
                                api.rooms(),
                                new SimpleRoom("test_name", 12)
                        ),
                        new CreateBookingFixture(
                                api.bookings(),
                                new SimpleBooking(
                                        null,
                                        "test_user_id",
                                        new SimpleRoom("test_name", 12),
                                        new LogicalSlot(
                                                Instant.now().plus(Duration.standardMinutes(15).getMillis()).getMillis() / 1000,
                                                Instant.now().plus(Duration.standardMinutes(45).getMillis()).getMillis() / 1000
                                        )
                                )
                        )
                ),
                new RequestHasStatusCodeAssertion(
                        get("http://localhost:7000/rooms")
                                .queryString("capacity", 10)
                                .queryString("timestamp_start", Instant.now().getMillis() / 1000)
                                .queryString(
                                        "timestamp_end",
                                        Instant.now().plus(Duration.standardHours(1).getMillis()).getMillis() / 1000
                                ).getHttpRequest(),
                        HttpStatus.NO_CONTENT_204
                )
        ).check();
    }
}