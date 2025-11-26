package fr.guddy.roombookings.domain.room;

import fr.guddy.roombookings.domain.room.matchers.HasCapacity;
import fr.guddy.roombookings.domain.room.matchers.HasMap;
import fr.guddy.roombookings.domain.room.matchers.HasName;
import org.cactoos.map.MapEntry;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.AllOf;
import org.junit.jupiter.api.Test;

final class NitriteRoomTest {

  @Test
  void isOK() {
    MatcherAssert.assertThat(
      "Nitrite room has name, capacity and map",
      new NitriteRoom(new SimpleRoom("test", 12)),
      new AllOf<>(
        new HasName("test"),
        new HasCapacity(12),
        new HasMap(new MapEntry<>("room_name", "test"), new MapEntry<>("room_capacity", 12))
      )
    );
  }
}
