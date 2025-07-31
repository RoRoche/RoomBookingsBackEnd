package fr.guddy.roombookings.domain.rooms;

import fr.guddy.roombookings.domain.room.NitriteRoom;
import fr.guddy.roombookings.domain.room.Room;
import org.dizitart.no2.*;
import org.dizitart.no2.filters.Filters;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.dizitart.no2.filters.Filters.eq;

public final class NitriteRooms implements Rooms {
    private static final String INDEX_ROOM_NAME = "room_name";

    private final NitriteCollection collection;

    public NitriteRooms(final NitriteCollection collection) {
        this.collection = collection;
    }

    public NitriteRooms(final Supplier<NitriteCollection> collectionSupplier) {
        this(collectionSupplier.get());
    }

    public NitriteRooms(final Nitrite database) {
        this(() -> {
            final NitriteCollection rooms = database.getCollection("rooms");
            if (!rooms.hasIndex(INDEX_ROOM_NAME)) {
                rooms.createIndex(INDEX_ROOM_NAME, IndexOptions.indexOptions(IndexType.Unique, true));
            }
            return rooms;
        });
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
    public List<Room> rooms() {
        return collection.find()
                .toList()
                .stream()
                .map(NitriteRoom::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> capableRooms(final int capacity) {
        return collection.find(Filters.gte("room_capacity", capacity))
                .toList()
                .stream()
                .map(NitriteRoom::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Room> namedRoom(final String name) {
        return collection.find(eq(INDEX_ROOM_NAME, name))
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
