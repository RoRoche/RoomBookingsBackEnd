package fr.guddy.roombookings.infra.requests;

import com.mashape.unirest.request.HttpRequestWithBody;
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

import static com.mashape.unirest.http.Unirest.delete;
import static org.assertj.core.api.Assertions.assertThat;

public final class DeleteBookingRequestTest {
    @RegisterExtension
    static ApiExternalExtension api = new ApiExternalExtension();

    @Test
    public void testNotFound() {
        new WithFixtureAssertion(
                new ChainedFixtures(
                        new ClearAllRoomsFixture(api.rooms()),
                        new ClearAllBookingsFixture(api.bookings())
                ),
                new RequestWithBodyAssertion(
                        new RequestHasStatusCodeAssertion(
                                delete("http://localhost:7000/bookings/12"),
                                HttpStatus.NOT_FOUND_404
                        ),
                        "No booking with id 12 were found"
                )
        ).check();
    }

    @Test
    public void testOk() {
        // given
        new ClearAllRoomsFixture(api.rooms()).perform();
        new ClearAllBookingsFixture(api.bookings()).perform();
        final SimpleRoom room = new SimpleRoom("test_name", 12);
        new CreateRoomFixture(api.rooms(), room).perform();
        final long timestampStart = Instant.now().getMillis() / 1000;
        final long timestampEnd = Instant.now().plus(Duration.standardHours(1).getMillis()).getMillis() / 1000;
        final long id = new CreateBookingFixture(
                api.bookings(),
                new SimpleBooking(
                        null,
                        "test_user_id",
                        room,
                        new LogicalSlot(
                                timestampStart,
                                timestampEnd
                        )
                )
        ).perform();

        // when
        final HttpRequestWithBody delete = delete(
                String.format(
                        "http://localhost:7000/bookings/%d",
                        id
                )
        );

        // then
        new RequestWithBodyAssertion(
                new RequestHasStatusCodeAssertion(delete, HttpStatus.OK_200),
                String.format(
                        "{\"id\":%d,\"user_id\":\"test_user_id\",\"room\":{\"name\":\"test_name\",\"capacity\":12},\"slot\":{\"timestamp_start\":%d,\"timestamp_end\":%d}}",
                        id,
                        timestampStart,
                        timestampEnd
                )
        ).check();
        assertThat(
                api.bookings().bookingById(id)
        ).isEmpty();
    }
}