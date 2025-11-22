package fr.guddy.roombookings.infra;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.bookings.NitriteBookings;
import fr.guddy.roombookings.domain.rooms.NitriteRooms;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.ports.DefaultPort;
import fr.guddy.roombookings.infra.ports.Port;
import org.dizitart.no2.Nitrite;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public final class ApiExternalExtension implements BeforeAllCallback, AfterAllCallback {

  private final Api api;
  private final Rooms rooms;
  private final Bookings bookings;

  private ApiExternalExtension(final Api api, final Rooms rooms, final Bookings bookings) {
    this.api = api;
    this.rooms = rooms;
    this.bookings = bookings;
  }

  public ApiExternalExtension(
    final Nitrite database,
    final Rooms rooms,
    final Bookings bookings,
    final Port port
  ) {
    this(new Api(database, rooms, bookings, port), rooms, bookings);
  }

  public ApiExternalExtension(final Nitrite database, final Port port) {
    this(database, new NitriteRooms(database), port);
  }

  public ApiExternalExtension(final Nitrite database, final Rooms rooms, final Port port) {
    this(database, new NitriteRooms(database), new NitriteBookings(database, rooms), port);
  }

  public ApiExternalExtension() {
    this(Nitrite.builder().openOrCreate(), new DefaultPort());
  }

  public Rooms rooms() {
    return rooms;
  }

  public Bookings bookings() {
    return bookings;
  }

  @Override
  public void beforeAll(final ExtensionContext context) throws InterruptedException {
    api.start();
    waitForServer();
  }

  @Override
  public void afterAll(final ExtensionContext context) {
    api.stop();
  }

  public void waitForServer() throws InterruptedException {
    int retries = 10;
    int delay = 200; // ms
    boolean isReady = false;

    while (retries-- > 0 && !isReady) {
      try {
        final HttpResponse<String> response = Unirest.get(
          String.format("http://localhost:%d/ready", api.port().value())
        ).asString();
        if (response.getStatus() == 200 && "READY".equals(response.getBody())) {
          isReady = true;
        }
      } catch (final UnirestException e) {
        Thread.sleep(delay);
      }
    }

    if (!isReady) {
      throw new RuntimeException("Server didn't start in time");
    }
  }
}
