package fr.guddy.roombookings.domain.room;

import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.dizitart.no2.Document;

import java.util.Map;

public final class NitriteRoom extends RoomEnvelope {
  private static final String DOCUMENT_KEY_NAME = "room_name";
  private static final String DOCUMENT_KEY_CAPACITY = "room_capacity";

  public NitriteRoom(Room delegate) {
    super(delegate);
  }

  public NitriteRoom(final Document document) {
    this(
      new SimpleRoom(
        document.get(DOCUMENT_KEY_NAME, String.class),
        document.get(DOCUMENT_KEY_CAPACITY, Integer.class)
      )
    );
  }

  @Override
  public Map<String, Object> map() {
    return new MapOf<String, Object>(
      new MapEntry<>(NitriteRoom.DOCUMENT_KEY_NAME, name()),
      new MapEntry<>(NitriteRoom.DOCUMENT_KEY_CAPACITY, capacity())
    );
  }
}
