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
package fr.guddy.roombookings.domain.booking;

import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.domain.slot.LogicalSlot;
import fr.guddy.roombookings.domain.slot.Slot;
import java.util.Map;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.dizitart.no2.collection.Document;

public final class NitriteBooking extends Booking.Envelope {

  private static final String DOCUMENT_KEY_ROOM_NAME = "room_name";
  private static final String DOCUMENT_KEY_USER_ID = "user_id";
  private static final String DOCUMENT_KEY_SLOT_TIMESTAMP_START = "slot_timestamp_start";
  private static final String DOCUMENT_KEY_SLOT_TIMESTAMP_END = "slot_timestamp_end";

  public NitriteBooking(final Booking delegate) {
    super(delegate);
  }

  public NitriteBooking(final Long id, final String userId, final Room room, final Slot slot) {
    this(new SimpleBooking(id, userId, room, slot));
  }

  public NitriteBooking(final Document document, final Rooms rooms) throws RoomNotFoundException {
    this(
      document.getId().getIdValue(),
      document.get(DOCUMENT_KEY_USER_ID, String.class),
      document.get(DOCUMENT_KEY_ROOM_NAME, String.class),
      document.get(DOCUMENT_KEY_SLOT_TIMESTAMP_START, Long.class),
      document.get(DOCUMENT_KEY_SLOT_TIMESTAMP_END, Long.class),
      rooms
    );
  }

  public NitriteBooking(
    final Long id,
    final String userId,
    final String roomName,
    final long timestampStart,
    final long timestampEnd,
    final Rooms rooms
  ) {
    this(
      id,
      userId,
      rooms.withName(roomName).orElseThrow(() -> new RoomNotFoundException(roomName)),
      new LogicalSlot(timestampStart, timestampEnd)
    );
  }

  @Override
  public Map<String, Object> map() {
    return new MapOf<>(
      new MapEntry<>(DOCUMENT_KEY_USER_ID, userId()),
      new MapEntry<>(DOCUMENT_KEY_ROOM_NAME, room().name()),
      new MapEntry<>(DOCUMENT_KEY_SLOT_TIMESTAMP_START, slot().timestampStart()),
      new MapEntry<>(DOCUMENT_KEY_SLOT_TIMESTAMP_END, slot().timestampEnd())
    );
  }
}
