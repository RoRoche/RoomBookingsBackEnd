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
package fr.guddy.roombookings.infra.routes;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.params.IntegerParameter;
import fr.guddy.roombookings.infra.params.OptionalParameter;
import fr.guddy.roombookings.infra.params.QueryParameter;
import fr.guddy.roombookings.infra.params.StringParameter;
import fr.guddy.roombookings.infra.requests.*;
import io.javalin.apibuilder.EndpointGroup;
import java.util.stream.Stream;

public final class RoomsRoute implements EndpointGroup {

  private final Rooms rooms;
  private final Bookings bookings;

  public RoomsRoute(final Rooms rooms, final Bookings bookings) {
    this.rooms = rooms;
    this.bookings = bookings;
  }

  @Override
  public void addEndpoints() {
    get((ctx) -> {
      final OptionalParameter<String> capacityParameter = new OptionalParameter<>(
        new QueryParameter("capacity", ctx)
      );
      final OptionalParameter<String> timestampStartParameter = new OptionalParameter<>(
        new QueryParameter("timestamp_start", ctx)
      );
      final Request request;
      if (
        Stream.of(capacityParameter, timestampStartParameter).allMatch((parameter) ->
          parameter.value().isPresent()
        )
      ) {
        request = new GetAvailableRoomsRequest(rooms, bookings, ctx);
      } else {
        request = capacityParameter
          .value()
          .<Request>map((capacity) ->
            new GetCapableRoomsRequest(
              rooms,
              new IntegerParameter(new StringParameter("capacity", capacity))
            )
          )
          .orElseGet(() -> new GetRoomsRequest(rooms));
      }
      request.perform(ctx);
    });
    get("/{name}", (ctx) -> new GetNamedRoomRequest(rooms, ctx).perform(ctx));
    get("/{name}/bookings", (ctx) ->
      new GetBookingsForRoomInSlotRequest(rooms, bookings, ctx).perform(ctx)
    );
    post("/{name}/bookings", (ctx) -> new PostBookingRequest(rooms, bookings, ctx).perform(ctx));
    post((ctx) -> new PostRoomRequest(rooms, ctx).perform(ctx));
  }
}
