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
    return this.collection.find()
      .toList()
      .stream()
      .map((document) -> (Room) new NitriteRoom(document))
      .toList();
  }

  @Override
  public List<Room> withCapacity(final int capacity) {
    return this.collection.find(where("room_capacity").gte(capacity))
      .toList()
      .stream()
      .map((document) -> (Room) new NitriteRoom(document))
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
