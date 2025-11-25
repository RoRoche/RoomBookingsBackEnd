package fr.guddy.roombookings.domain.room;

import fr.guddy.roombookings.domain.room.matchers.HasCapacity;
import fr.guddy.roombookings.domain.room.matchers.HasMap;
import fr.guddy.roombookings.domain.room.matchers.HasName;
import org.apache.commons.lang3.tuple.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.AllOf;
import org.junit.jupiter.api.Test;

final class JsonRoomTest {

  @Test
  void isOK() {
    MatcherAssert.assertThat(
      "JSON room has name, capacity and map",
      new JsonRoom(new SimpleRoom("test", 12)),
      new AllOf<>(
        new HasName("test"),
        new HasCapacity(12),
        new HasMap(Pair.of("name", "test"), Pair.of("capacity", 12))
      )
    );
  }
}
