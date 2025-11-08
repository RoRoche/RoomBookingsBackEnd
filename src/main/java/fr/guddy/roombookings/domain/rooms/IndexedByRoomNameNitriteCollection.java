package fr.guddy.roombookings.domain.rooms;

import org.cactoos.Scalar;
import org.dizitart.no2.IndexOptions;
import org.dizitart.no2.IndexType;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;

public final class IndexedByRoomNameNitriteCollection implements Scalar<NitriteCollection> {
    private static final String INDEX_ROOM_NAME = "room_name";

    private final Nitrite database;

    public IndexedByRoomNameNitriteCollection(Nitrite database) {
        this.database = database;
    }

    @Override
    public NitriteCollection value() {
        final NitriteCollection rooms = database.getCollection("rooms");
        if (!rooms.hasIndex(INDEX_ROOM_NAME)) {
            rooms.createIndex(INDEX_ROOM_NAME, IndexOptions.indexOptions(IndexType.Unique, true));
        }
        return rooms;
    }
}
