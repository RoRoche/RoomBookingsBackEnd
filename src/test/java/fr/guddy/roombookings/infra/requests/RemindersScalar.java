package fr.guddy.roombookings.infra.requests;

import static com.mashape.unirest.http.Unirest.get;

import fr.guddy.roombookings.domain.booking.SimpleBooking;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.room.SimpleRoom;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import org.cactoos.Scalar;
import org.joda.time.Duration;
import org.joda.time.Instant;

public final class RemindersScalar implements Scalar<Reminders> {

  private final ApiExternalExtension api;
  private final long baseTs;

  public RemindersScalar(final ApiExternalExtension api, final long baseTs) {
    this.api = api;
    this.baseTs = baseTs;
  }

  @Override
  public Reminders value() throws Exception {
    this.api.rooms().clearAll();
    this.api.bookings().clearAll();
    final Room room = new SimpleRoom("test_name", 12);
    this.api.rooms().create(room);
    final long tsMinus45 =
      Instant.ofEpochSecond(this.baseTs)
        .minus(Duration.standardMinutes(45).getMillis())
        .getMillis() /
      1000;
    final long tsMinus15 =
      Instant.ofEpochSecond(this.baseTs)
        .minus(Duration.standardMinutes(15).getMillis())
        .getMillis() /
      1000;
    final long tsPlus15 =
      Instant.ofEpochSecond(this.baseTs)
        .plus(Duration.standardMinutes(15).getMillis())
        .getMillis() /
      1000;
    final long tsPlus45 =
      Instant.ofEpochSecond(this.baseTs)
        .plus(Duration.standardMinutes(45).getMillis())
        .getMillis() /
      1000;
    // booking 1: [-45 ; -15]
    this.api.bookings().create(
      new SimpleBooking(null, "test_user_id", room, new LogicalSlot(tsMinus45, tsMinus15))
    );
    // booking 2: [-15 ; +15]
    final long idMinus15ToPlus15 = this.api.bookings().create(
      new SimpleBooking(null, "test_user_id", room, new LogicalSlot(tsMinus15, tsPlus15))
    );
    // booking 3: [+15 ; +45]
    final long idPlus15To45 = this.api.bookings().create(
      new SimpleBooking(null, "test_user_id", room, new LogicalSlot(tsPlus15, tsPlus45))
    );
    // HTTP call
    final HttpTestCase<String> resp = () ->
      get("http://localhost:%d/bookings".formatted(this.api.port().value()))
        .queryString("user_id", "test_user_id")
        .asString();
    return new Reminders(resp, idMinus15ToPlus15, idPlus15To45, tsMinus15, tsPlus15, tsPlus45);
  }
}
