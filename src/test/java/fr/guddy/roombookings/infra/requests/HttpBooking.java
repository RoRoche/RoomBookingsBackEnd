package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.slot.Slot;
import fr.guddy.roombookings.infra.HttpTestCase;
import java.util.Map;

public record HttpBooking(
  Long id,
  String userId,
  Room room,
  Slot slot,
  HttpTestCase<String> testCase
) implements Booking {
  @Override
  public Map<String, Object> map() {
    return Map.of();
  }
}
