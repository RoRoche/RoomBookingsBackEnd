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
package fr.guddy.roombookings.domain.rooms;

import static org.dizitart.no2.filters.FluentFilter.where;

import fr.guddy.roombookings.domain.room.NitriteRoom;
import fr.guddy.roombookings.domain.room.Room;
import java.util.List;
import java.util.Optional;
import org.cactoos.Scalar;
import org.cactoos.scalar.Unchecked;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.NitriteCollection;
import org.dizitart.no2.filters.Filter;

public final class NitriteRooms implements Rooms {

  private final NitriteCollection collection;

  public NitriteRooms(final NitriteCollection collection) {
    this.collection = collection;
  }

  public NitriteRooms(final Scalar<NitriteCollection> collection) {
    this(new Unchecked<>(collection).value());
  }

  public NitriteRooms(final Nitrite database) {
    this(new IndexedByRoomNameNitriteCollection(database));
  }

  @Override
  public Long create(final Room room) {
    return this.collection.insert(Document.createDocument(new NitriteRoom(room).map()))
      .iterator()
      .next()
      .getIdValue();
  }

  @Override
  public List<Room> all() {
    return this.collection.find().toList().stream().<Room>map(NitriteRoom::new).toList();
  }

  @Override
  public List<Room> withCapacity(final int capacity) {
    return this.collection.find(where("room_capacity").gte(capacity))
      .toList()
      .stream()
      .<Room>map(NitriteRoom::new)
      .toList();
  }

  @Override
  public Optional<Room> withName(final String name) {
    return this.collection.find(where("room_name").eq(name))
      .toList()
      .stream()
      .findFirst()
      .map(NitriteRoom::new);
  }

  @Override
  public int clearAll() {
    return this.collection.remove(Filter.ALL).getAffectedCount();
  }
}
