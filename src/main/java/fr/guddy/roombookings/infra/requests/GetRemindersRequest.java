package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.booking.JsonBooking;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.infra.params.Parameter;
import fr.guddy.roombookings.infra.params.QueryParameter;
import fr.guddy.roombookings.infra.params.RequiredParameter;
import io.javalin.http.Context;
import java.util.List;
import org.eclipse.jetty.http.HttpStatus;

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
    this(bookings, new RequiredParameter<>(new QueryParameter("user_id", context)));
  }

  @Override
  public void perform(final Context context) {
    final List<Booking> reminders = bookings.forUserFromStartDate(userId, 1764352800);
    if (reminders.isEmpty()) {
      context.status(HttpStatus.NO_CONTENT_204);
    } else {
      context
        .json(reminders.stream().map(JsonBooking::new).map(JsonBooking::map).toList())
        .status(HttpStatus.OK_200);
    }
  }
}
