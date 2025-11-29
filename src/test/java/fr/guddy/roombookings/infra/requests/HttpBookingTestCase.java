package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.booking.SimpleBooking;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.domain.slot.Slot;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import org.cactoos.Func;
import org.cactoos.Scalar;

public final class HttpBookingTestCase implements Scalar<HttpBooking> {

  private final ApiExternalExtension api;
  private final long timestampStart;
  private final long timestampEnd;
  private final Func<Long, HttpTestCase<String>> testCaseFunc;

  public HttpBookingTestCase(
    final ApiExternalExtension api,
    final long timestampStart,
    final long timestampEnd,
    final Func<Long, HttpTestCase<String>> testCaseFunc
  ) {
    this.api = api;
    this.timestampStart = timestampStart;
    this.timestampEnd = timestampEnd;
    this.testCaseFunc = testCaseFunc;
  }

  @Override
  public HttpBooking value() throws Exception {
    this.api.rooms().clearAll();
    this.api.bookings().clearAll();
    final Room room = new SimpleRoom("test_name", 12);
    this.api.rooms().create(room);
    final Slot slot = new LogicalSlot(this.timestampStart, this.timestampEnd);
    final long id = this.api.bookings().create(new SimpleBooking(null, "test_user_id", room, slot));
    return new HttpBooking(id, "test_user_id", room, slot, this.testCaseFunc.apply(id));
  }
}
