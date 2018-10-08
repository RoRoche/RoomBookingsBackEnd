package fr.guddy.roombookings.domain.rooms;

import fr.guddy.roombookings.domain.room.NitriteRoom;
import fr.guddy.roombookings.domain.room.Room;
import io.vavr.Lazy;
import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.dizitart.no2.IndexOptions.indexOptions;
import static org.dizitart.no2.IndexType.Unique;
import static org.dizitart.no2.filters.Filters.eq;
import static org.dizitart.no2.filters.Filters.gte;

public final class NitriteRooms implements Rooms {
    private final Lazy<NitriteCollection> collection;

    public NitriteRooms(final NitriteCollection collection) {
        this.collection = Lazy.of(() -> {
            if (!collection.hasIndex("name")) {
                collection.createIndex("name", indexOptions(Unique, true));
            }
            return collection;
        });
    }

    @Override
    public void create(final Room room) {
        collection.get().insert(
                new Document(
                        new NitriteRoom(room).map()
                )
        );
    }

    @Override
    public List<Room> rooms() {
        return collection.get().find()
                .toList()
                .stream()
                .map(NitriteRoom::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> capableRooms(final int capacity) {
        return collection.get().find(gte("capacity", capacity))
                .toList()
                .stream()
                .map(NitriteRoom::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Room> namedRoom(final String name) {
        return collection.get().find(eq("name", name))
                .toList()
                .stream()
                .findFirst()
                .map(NitriteRoom::new);
    }
}
