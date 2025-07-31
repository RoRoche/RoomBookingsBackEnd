package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.booking.JsonBooking;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.infra.params.Parameter;
import fr.guddy.roombookings.infra.params.QueryParameter;
import fr.guddy.roombookings.infra.params.RequiredParameter;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.time.Instant;

import java.util.List;
import java.util.stream.Collectors;

public final class GetRemindersRequest implements Request {

    private final Bookings bookings;
    private final String userId;

    public GetRemindersRequest(final Bookings bookings, final String userId) {
        this.bookings = bookings;
        this.userId = userId;
    }

    public GetRemindersRequest(final Bookings bookings, final Parameter<String> userId) {
        this(bookings, userId.value());
    }

    public GetRemindersRequest(final Bookings bookings, final Context context) {
        this(
                bookings,
                new RequiredParameter<>(
                        new QueryParameter("user_id", context)
                )
        );
    }

    @Override
    public void perform(final Context context) {
        final List<Booking> reminders = bookings.bookingsForUserFromStartDate(
                userId,
                Instant.now().getMillis() / 1000
        );
        if (reminders.isEmpty()) {
            context.status(HttpStatus.NO_CONTENT_204);
        } else {
            context.json(
                    reminders.stream()
                            .map(JsonBooking::new)
                            .map(JsonBooking::map)
                            .collect(Collectors.toList())
            ).status(HttpStatus.OK_200);
        }
    }
}
