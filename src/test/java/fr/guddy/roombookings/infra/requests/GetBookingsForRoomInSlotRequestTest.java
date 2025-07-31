package fr.guddy.roombookings.infra.requests;

import com.mashape.unirest.request.HttpRequest;
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

final class GetBookingsForRoomInSlotRequestTest {
    @RegisterExtension
    static ApiExternalExtension api = new ApiExternalExtension();

    @Test
    void testNoContent() {
        new WithFixtureAssertion(
                new ChainedFixtures(
                        new ClearAllRoomsFixture(api.rooms()),
                        new ClearAllBookingsFixture(api.bookings()),
                        new CreateRoomFixture(
                                api.rooms(),
                                new SimpleRoom("test_name", 12)
                        )
                ),
                new RequestHasStatusCodeAssertion(
                        get("http://localhost:7000/rooms/test_name/bookings")
                                .queryString(
                                        "timestamp_start",
                                        Instant.now().getMillis() / 1000
                                )
                                .queryString(
                                        "timestamp_end",
                                        Instant.now().plus(Duration.standardHours(1).getMillis()).getMillis() / 1000
                                )
                                .getHttpRequest(),
                        HttpStatus.NO_CONTENT_204
                )
        ).check();
    }

    @Test
    public void testOk() {
        // given
        final long timestampStart = Instant.now().plus(Duration.standardMinutes(15).getMillis()).getMillis() / 1000;
        final long timestampEnd = Instant.now().plus(Duration.standardMinutes(45).getMillis()).getMillis() / 1000;
        final SimpleRoom room = new SimpleRoom("test_name", 12);
        new ChainedFixtures(
                new ClearAllRoomsFixture(api.rooms()),
                new ClearAllBookingsFixture(api.bookings()),
                new CreateRoomFixture(api.rooms(), room)
        ).perform();
        final Long id = new CreateBookingFixture(
                api.bookings(),
                new SimpleBooking(
                        null,
                        "test_user_id",
                        room,
                        new LogicalSlot(timestampStart, timestampEnd)
                )
        ).perform();

        // when
        final HttpRequest request = get("http://localhost:7000/rooms/test_name/bookings")
                .queryString(
                        "timestamp_start",
                        Instant.now().getMillis() / 1000
                )
                .queryString(
                        "timestamp_end",
                        Instant.now().plus(Duration.standardHours(1).getMillis()).getMillis() / 1000
                )
                .getHttpRequest();

        // then
        new RequestWithBodyAssertion(
                new RequestHasStatusCodeAssertion(request, HttpStatus.OK_200),
                String.format(
                        "[{\"id\":%d,\"user_id\":\"test_user_id\",\"room\":{\"name\":\"test_name\",\"capacity\":12},\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}]",
                        id,
                        timestampStart,
                        timestampEnd
                )
        ).check();
    }

    @Test
    void testRoomNotFound() {
        new WithFixtureAssertion(
                new ChainedFixtures(
                        new ClearAllRoomsFixture(api.rooms()),
                        new ClearAllBookingsFixture(api.bookings())
                ),
                new RequestWithBodyAssertion(
                        new RequestHasStatusCodeAssertion(
                                get("http://localhost:7000/rooms/test_name/bookings")
                                        .queryString(
                                                "timestamp_start",
                                                Instant.now().getMillis() / 1000
                                        )
                                        .queryString(
                                                "timestamp_end",
                                                Instant.now().plus(Duration.standardHours(1).getMillis()).getMillis() / 1000
                                        )
                                        .getHttpRequest(),
                                HttpStatus.NOT_FOUND_404
                        ),
                        "No room found for name 'test_name'"
                )
        ).check();
    }
}