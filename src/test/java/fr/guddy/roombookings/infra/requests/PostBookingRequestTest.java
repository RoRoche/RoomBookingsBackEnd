package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.booking.SimpleBooking;
import fr.guddy.roombookings.domain.fixtures.*;
import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.assertions.WithFixtureAssertion;
import fr.guddy.roombookings.infra.assertions.requests.RequestHasStatusCodeAssertion;
import fr.guddy.roombookings.infra.assertions.requests.RequestWithBodyAssertion;
import fr.guddy.roombookings.infra.assertions.requests.RequestWithLocationHeaderAssertion;
import fr.guddy.roombookings.infra.assertions.requests.RequestWithPartialBodyAssertion;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.mashape.unirest.http.Unirest.post;

public final class PostBookingRequestTest {
    @RegisterExtension
    public static final ApiExternalExtension api = new ApiExternalExtension();

    @Test
    public void testOK() {
        final long timestampStart = Instant.now().getMillis() / 1000;
        final long timestampEnd = Instant.now().plus(Duration.standardHours(1).getMillis()).getMillis() / 1000;
        new WithFixtureAssertion(
                new ChainedFixtures(
                        new ClearAllRoomsFixture(api.rooms()),
                        new ClearAllBookingsFixture(api.bookings()),
                        new CreateRoomFixture(
                                api.rooms(),
                                new SimpleRoom("test_name", 12)
                        )
                ),
                new RequestWithLocationHeaderAssertion(
                        new RequestWithPartialBodyAssertion(
                                new RequestHasStatusCodeAssertion(
                                        post("http://localhost:7000/rooms/test_name/bookings")
                                                .body(
                                                        String.format(
                                                                "{\"user_id\":\"test_user_id\",\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}",
                                                                timestampStart,
                                                                timestampEnd
                                                        )
                                                )
                                                .getHttpRequest(),
                                        HttpStatus.CREATED_201
                                ),
                                String.format(
                                        "\"user_id\":\"test_user_id\",\"room\":{\"name\":\"test_name\",\"capacity\":12},\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}",
                                        timestampStart,
                                        timestampEnd
                                )
                        ),
                        "/bookings/"
                )
        ).check();
    }

    @Test
    public void testConflict() {
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
                new RequestWithBodyAssertion(
                        new RequestHasStatusCodeAssertion(
                                post("http://localhost:7000/rooms/test_name/bookings")
                                        .body(
                                                String.format(
                                                        "{\"user_id\":\"test_user_id\",\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}",
                                                        Instant.now().getMillis() / 1000,
                                                        Instant.now().plus(Duration.standardHours(1).getMillis()).getMillis() / 1000
                                                )
                                        )
                                        .getHttpRequest(),
                                HttpStatus.CONFLICT_409
                        ),
                        "Room named 'test_name' already booked on this slot"
                )
        ).check();
    }

    @Test
    public void testRoomNotFound() {
        new WithFixtureAssertion(
                new ChainedFixtures(
                        new ClearAllRoomsFixture(api.rooms()),
                        new ClearAllBookingsFixture(api.bookings())
                ),
                new RequestWithBodyAssertion(
                        new RequestHasStatusCodeAssertion(
                                post("http://localhost:7000/rooms/test_name/bookings")
                                        .body(
                                                String.format(
                                                        "{\"user_id\":\"test_user_id\",\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}",
                                                        Instant.now().getMillis() / 1000,
                                                        Instant.now().plus(Duration.standardHours(1).getMillis()).getMillis() / 1000
                                                )
                                        )
                                        .getHttpRequest(),
                                HttpStatus.NOT_FOUND_404
                        ),
                        "No room found for name 'test_name'"
                )
        ).check();
    }
}