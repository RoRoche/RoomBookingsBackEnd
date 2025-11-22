package fr.guddy.roombookings.domain.room;

import java.util.Map;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;

public record SimpleRoom(String name, int capacity) implements Room {
  @Override
  public Map<String, Object> map() {
    return new MapOf<String, Object>(
      new MapEntry<>("name", this.name),
      new MapEntry<>("capacity", this.capacity)
    );
  }
}
