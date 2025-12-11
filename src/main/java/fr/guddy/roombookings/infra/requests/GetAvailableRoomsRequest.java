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
package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.domain.slot.Slot;
import fr.guddy.roombookings.infra.params.*;
import io.javalin.http.Context;
import java.util.List;
import org.eclipse.jetty.http.HttpStatus;

public final class GetAvailableRoomsRequest implements Request {

  private final Rooms rooms;
  private final Bookings bookings;
  private final int capacity;
  private final Slot slot;

  public GetAvailableRoomsRequest(
    final Rooms rooms,
    final Bookings bookings,
    final int capacity,
    final Slot slot
  ) {
    this.rooms = rooms;
    this.bookings = bookings;
    this.capacity = capacity;
    this.slot = slot;
  }

  public GetAvailableRoomsRequest(
    final Rooms rooms,
    final Bookings bookings,
    final Parameter<Integer> capacity,
    final Parameter<Long> timestampStart,
    final Parameter<Long> timestampEnd
  ) {
    this(
      rooms,
      bookings,
      capacity.value(),
      new LogicalSlot(timestampStart.value(), timestampEnd.value())
    );
  }

  public GetAvailableRoomsRequest(
    final Rooms rooms,
    final Bookings bookings,
    final Context context
  ) {
    this(
      rooms,
      bookings,
      new IntegerParameter(new RequiredParameter<>(new QueryParameter("capacity", context))),
      new LongParameter(new RequiredParameter<>(new QueryParameter("timestamp_start", context))),
      new LongParameter(new RequiredParameter<>(new QueryParameter("timestamp_end", context)))
    );
  }

  @Override
  public void perform(final Context context) {
    final List<Room> availableRooms = rooms
      .withCapacity(capacity)
      .stream()
      .filter((room) -> bookings.forRoomInSlot(room, slot).isEmpty())
      .toList();
    if (availableRooms.isEmpty()) {
      context.status(HttpStatus.NO_CONTENT_204);
    } else {
      context
        .json(availableRooms.stream().map(JsonRoom::new).map(JsonRoom::map).toList())
        .status(HttpStatus.OK_200);
    }
  }
}
