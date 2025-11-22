package fr.guddy.roombookings.domain.slot;

import java.util.Map;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;

public record LogicalSlot(long timestampStart, long timestampEnd) implements Slot {
  @Override
  public long timestampStart() {
    return Long.min(timestampStart, timestampEnd);
  }

  @Override
  public long timestampEnd() {
    return Long.max(timestampStart, timestampEnd);
  }

  @Override
  public Map<String, Object> map() {
    return new MapOf<String, Object>(
      new MapEntry<>("timestampStart", this.timestampStart),
      new MapEntry<>("timestampEnd", this.timestampEnd)
    );
  }
}
