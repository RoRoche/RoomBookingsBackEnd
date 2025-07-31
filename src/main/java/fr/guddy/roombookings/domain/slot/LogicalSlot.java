package fr.guddy.roombookings.domain.slot;

import java.util.HashMap;
import java.util.Map;

public record LogicalSlot(long timestampStart, long timestampEnd) implements Slot {
    public LogicalSlot(final long timestampStart, final long timestampEnd) {
        this.timestampStart = Long.min(timestampStart, timestampEnd);
        this.timestampEnd = Long.max(timestampStart, timestampEnd);
    }

    @Override
    public Map<String, Object> map() {
        final Map<String, Object> map = new HashMap<>();
        map.put("timestampStart", timestampStart);
        map.put("timestampEnd", timestampEnd);
        return map;
    }
}
