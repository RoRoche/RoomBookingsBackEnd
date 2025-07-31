package fr.guddy.roombookings.domain.slot;

import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;

import java.util.HashMap;
import java.util.Map;

public record LogicalSlot(long timestampStart, long timestampEnd) implements Slot {
    public LogicalSlot(final long timestampStart, final long timestampEnd) {
        this.timestampStart = Long.min(timestampStart, timestampEnd);
        this.timestampEnd = Long.max(timestampStart, timestampEnd);
    }

    @Override
    public Map<String, Object> map() {
        return new MapOf<String, Object>(
                new MapEntry<>("timestampStart", timestampStart),
                new MapEntry<>("timestampEnd", timestampEnd)
        );
    }
}
