/*
 * MIT License
 *
 * Copyright (c) 2018-2025 Romain Rochegude
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package fr.guddy.roombookings.infra;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.bookings.NitriteBookings;
import fr.guddy.roombookings.domain.rooms.NitriteRooms;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.ports.Port;
import fr.guddy.roombookings.infra.ports.SimplePort;
import java.util.stream.IntStream;
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
    this(Nitrite.builder().openOrCreate(), new SimplePort(0));
  }

  public Rooms rooms() {
    return this.rooms;
  }

  public Bookings bookings() {
    return this.bookings;
  }

  public Port port() {
    return this.api.port();
  }

  @Override
  public void beforeAll(final ExtensionContext context) throws InterruptedException {
    this.api.start();
    waitForServer();
  }

  @Override
  public void afterAll(final ExtensionContext context) {
    this.api.stop();
  }

  public void waitForServer() {
    final int maxRetries = 10;
    final int delay = 200; // ms
    final boolean isReady = IntStream.range(0, maxRetries).anyMatch((final int attempt) -> {
      try {
        final HttpResponse<String> response = Unirest.get(
          String.format("http://localhost:%d/ready", this.api.port().value())
        ).asString();

        return response.getStatus() == 200 && "READY".equals(response.getBody());
      } catch (final UnirestException e) {
        try {
          Thread.sleep(delay);
        } catch (final InterruptedException ie) {
          Thread.currentThread().interrupt();
        }
        return false;
      }
    });

    if (!isReady) {
      throw new RuntimeException("Server didn't start in time");
    }
  }
}
