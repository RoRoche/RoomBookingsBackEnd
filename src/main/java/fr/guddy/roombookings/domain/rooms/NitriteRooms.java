package fr.guddy.roombookings.domain.rooms;

import fr.guddy.roombookings.domain.room.NitriteRoom;
import fr.guddy.roombookings.domain.room.Room;
import org.dizitart.no2.*;
import org.dizitart.no2.filters.Filters;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.dizitart.no2.filters.Filters.eq;

public final class NitriteRooms implements Rooms {
    private final NitriteCollection collection;

    public NitriteRooms(final NitriteCollection collection) {
        this.collection = collection;
    }

    public NitriteRooms(final Supplier<NitriteCollection> collectionSupplier) {
        this(collectionSupplier.get());
    }

    public NitriteRooms(final Nitrite database) {
        this(new IndexedByRoomNameNitriteCollection(database));
    }

    @Override
    public Long create(final Room room) {
        return collection.insert(
                new Document(
                        new NitriteRoom(room).map()
                )
        ).iterator().next().getIdValue();
    }

    @Override
    public List<Room> all() {
        return collection.find()
                .toList()
                .stream()
                .map(document -> (Room) new NitriteRoom(document))
                .toList();
    }

    @Override
    public List<Room> withCapacity(final int capacity) {
        return collection.find(Filters.gte("room_capacity", capacity))
                .toList()
                .stream()
                .map(document -> (Room) new NitriteRoom(document))
                .toList();
    }

    @Override
    public Optional<Room> withName(final String name) {
        return collection.find(eq("room_name", name))
                .toList()
                .stream()
                .findFirst()
                .map(NitriteRoom::new);
    }

    @Override
    public int clearAll() {
        return collection.remove(Filters.ALL).getAffectedCount();
    }
}
