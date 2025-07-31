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

public final class GetRemindersRequestTest {
    @RegisterExtension
    public static final ApiExternalExtension api = new ApiExternalExtension();

    @Test
    public void testMissingParameter() {
        new WithFixtureAssertion(
                new ChainedFixtures(
                        new ClearAllRoomsFixture(api.rooms()),
                        new ClearAllBookingsFixture(api.bookings())
                ),
                new RequestWithBodyAssertion(
                        new RequestHasStatusCodeAssertion(
                                get("http://localhost:7000/bookings")
                                        .getHttpRequest(),
                                HttpStatus.BAD_REQUEST_400
                        ),
                        "Parameter named 'user_id' is missing"
                )
        ).check();
    }

    @Test
    public void testNoContent() {
        new WithFixtureAssertion(
                new ChainedFixtures(
                        new ClearAllRoomsFixture(api.rooms()),
                        new ClearAllBookingsFixture(api.bookings())
                ),
                new RequestHasStatusCodeAssertion(
                        get("http://localhost:7000/bookings")
                                .queryString("user_id", "test@test.com")
                                .getHttpRequest(),
                        HttpStatus.NO_CONTENT_204
                )
        ).check();
    }

    @Test
    public void testContent() {
        // given
        final long nowMinus45m = Instant.now().minus(Duration.standardMinutes(45).getMillis()).getMillis() / 1000;
        final long nowMinus15m = Instant.now().minus(Duration.standardMinutes(15).getMillis()).getMillis() / 1000;
        final long nowPlus15m = Instant.now().plus(Duration.standardMinutes(15).getMillis()).getMillis() / 1000;
        final long nowPlus45m = Instant.now().plus(Duration.standardMinutes(45).getMillis()).getMillis() / 1000;
        final SimpleRoom room = new SimpleRoom("test_name", 12);
        new ChainedFixtures(
                new ClearAllRoomsFixture(api.rooms()),
                new ClearAllBookingsFixture(api.bookings()),
                new CreateRoomFixture(api.rooms(), room),
                new CreateBookingFixture(
                        api.bookings(),
                        new SimpleBooking(
                                null,
                                "test@test.com",
                                room,
                                new LogicalSlot(nowMinus45m, nowMinus15m)
                        )
                )
        ).perform();
        final long idMinus15ToPlus15m = new CreateBookingFixture(
                api.bookings(),
                new SimpleBooking(
                        null,
                        "test@test.com",
                        room,
                        new LogicalSlot(nowMinus15m, nowPlus15m)
                )
        ).perform();
        final long id15To45m = new CreateBookingFixture(
                api.bookings(),
                new SimpleBooking(
                        null,
                        "test@test.com",
                        room,
                        new LogicalSlot(nowPlus15m, nowPlus45m)
                )
        ).perform();

        // when
        final HttpRequest request = get("http://localhost:7000/bookings")
                .queryString("user_id", "test@test.com")
                .getHttpRequest();

        // then
        new RequestWithBodyAssertion(
                new RequestHasStatusCodeAssertion(request, HttpStatus.OK_200),
                String.format(
                        "[" +
                                "{\"id\":%d,\"user_id\":\"test@test.com\",\"room\":{\"name\":\"test_name\",\"capacity\":12},\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}," +
                                "{\"id\":%d,\"user_id\":\"test@test.com\",\"room\":{\"name\":\"test_name\",\"capacity\":12},\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}" +
                                "]",
                        idMinus15ToPlus15m,
                        nowMinus15m,
                        nowPlus15m,
                        id15To45m,
                        nowPlus15m,
                        nowPlus45m
                )
        ).check();
    }
}