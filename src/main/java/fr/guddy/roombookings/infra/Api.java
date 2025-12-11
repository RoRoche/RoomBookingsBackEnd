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

import static io.javalin.apibuilder.ApiBuilder.path;

import fr.guddy.roombookings.domain.bookings.*;
import fr.guddy.roombookings.domain.rooms.CreateRoomConflictException;
import fr.guddy.roombookings.domain.rooms.NitriteRooms;
import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.exceptions.*;
import fr.guddy.roombookings.infra.params.exceptions.MissingParameterException;
import fr.guddy.roombookings.infra.params.exceptions.NotProcessableParameterException;
import fr.guddy.roombookings.infra.ports.DefaultPort;
import fr.guddy.roombookings.infra.ports.HerokuAssignedPort;
import fr.guddy.roombookings.infra.ports.Port;
import fr.guddy.roombookings.infra.ports.SimplePort;
import fr.guddy.roombookings.infra.routes.BookingsRoute;
import fr.guddy.roombookings.infra.routes.ReadinessRoute;
import fr.guddy.roombookings.infra.routes.RoomsRoute;
import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.dizitart.no2.Nitrite;

public final class Api implements Application, Exposed {

  private final Javalin app;
  private final Nitrite database;
  private final Port port;

  public Api(final Javalin app, final Nitrite database, final Port port) {
    this.app = app;
    this.database = database;
    this.port = port;
  }

  public Api(final Nitrite database, final Rooms rooms, final Bookings bookings, final Port port) {
    this(
      Javalin.create((config) -> {
        config.router.apiBuilder(() -> {
          path("rooms", new RoomsRoute(rooms, bookings));
          path("bookings", new BookingsRoute(bookings));
          path("ready", new ReadinessRoute());
        });
        config.bundledPlugins.enableCors((cors) ->
          cors.addRule(CorsPluginConfig.CorsRule::anyHost)
        );
      })
        .exception(NotProcessableParameterException.class, new NotProcessableParameterResponse())
        .exception(MissingParameterException.class, new MissingParameterResponse())
        .exception(RoomNotFoundException.class, new RoomNotFoundResponse())
        .exception(BookingNotFoundException.class, new BookingNotFoundResponse())
        .exception(BookingNotDeletedException.class, new BookingNotDeletedResponse())
        .exception(CreateRoomConflictException.class, new CreateRoomConflictResponse())
        .exception(CreateBookingConflictException.class, new CreateBookingConflictResponse()),
      database,
      port
    );
  }

  public Api(final Nitrite database, final Port port) {
    this(database, new NitriteRooms(database), port);
  }

  public Api(final Nitrite database, final Rooms rooms, final Port port) {
    this(database, rooms, new NitriteBookings(database, rooms), port);
  }

  public Api() {
    this(Nitrite.builder().openOrCreate(), new HerokuAssignedPort(new DefaultPort()));
  }

  @Override
  public void start() {
    this.app.start(this.port.value());
  }

  @Override
  public void stop() {
    this.database.close();
    this.app.stop();
  }

  @Override
  public Port port() {
    return new SimplePort(this.app.port());
  }
}
