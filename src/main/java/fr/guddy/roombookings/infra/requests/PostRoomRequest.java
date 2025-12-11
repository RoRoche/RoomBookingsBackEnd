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

import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.CreateRoomConflictException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import io.javalin.http.Context;
import org.dizitart.no2.exceptions.UniqueConstraintException;
import org.eclipse.jetty.http.HttpStatus;

public final class PostRoomRequest implements Request {

  private final Rooms rooms;
  private final Room room;

  public PostRoomRequest(final Rooms rooms, final Room room) {
    this.rooms = rooms;
    this.room = room;
  }

  public PostRoomRequest(final Rooms rooms, final Context context) {
    this(rooms, new JsonRoom(context.body()));
  }

  @Override
  public void perform(final Context context) {
    try {
      rooms.create(room);
    } catch (final UniqueConstraintException exception) {
      throw new CreateRoomConflictException(exception, room.name());
    }
    context
      .header("location", String.format("/rooms/%s", room.name()))
      .status(HttpStatus.CREATED_201);
  }
}
