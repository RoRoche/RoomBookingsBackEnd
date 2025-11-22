package fr.guddy.roombookings.domain.room;

import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;

import java.util.Map;

public record SimpleRoom(String name, int capacity) implements Room {

  @Override
  public Map<String, Object> map() {
    return new MapOf<String, Object>(
      new MapEntry<>("name", this.name),
      new MapEntry<>("capacity", this.capacity)
    );
  }
}
