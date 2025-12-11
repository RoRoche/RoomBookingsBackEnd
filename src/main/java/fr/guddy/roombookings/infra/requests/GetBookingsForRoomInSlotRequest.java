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

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.booking.JsonBooking;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.domain.slot.Slot;
import fr.guddy.roombookings.infra.params.*;
import io.javalin.http.Context;
import java.util.List;
import org.eclipse.jetty.http.HttpStatus;

public final class GetBookingsForRoomInSlotRequest implements Request {

  private final Bookings bookings;
  private final Room room;
  private final Slot slot;

  public GetBookingsForRoomInSlotRequest(
    final Bookings bookings,
    final Room room,
    final Slot slot
  ) {
    this.bookings = bookings;
    this.room = room;
    this.slot = slot;
  }

  public GetBookingsForRoomInSlotRequest(
    final Rooms rooms,
    final Bookings bookings,
    final Context context
  ) {
    this(
      rooms,
      bookings,
      new RequiredParameter<>(new PathParameter("name", context)),
      new LongParameter(new RequiredParameter<>(new QueryParameter("timestamp_start", context))),
      new LongParameter(new RequiredParameter<>(new QueryParameter("timestamp_end", context)))
    );
  }

  public GetBookingsForRoomInSlotRequest(
    final Rooms rooms,
    final Bookings bookings,
    final Parameter<String> roomName,
    final Parameter<Long> timestampStart,
    final Parameter<Long> timestampEnd
  ) {
    this(
      bookings,
      rooms
        .withName(roomName.value())
        .orElseThrow(() -> new RoomNotFoundException(roomName.value())),
      new LogicalSlot(timestampStart.value(), timestampEnd.value())
    );
  }

  @Override
  public void perform(final Context context) {
    final List<Booking> existingBookings = bookings.forRoomInSlot(room, slot);
    if (existingBookings.isEmpty()) {
      context.status(HttpStatus.NO_CONTENT_204);
    } else {
      context
        .json(existingBookings.stream().map(JsonBooking::new).map(JsonBooking::map).toList())
        .status(HttpStatus.OK_200);
    }
  }
}
