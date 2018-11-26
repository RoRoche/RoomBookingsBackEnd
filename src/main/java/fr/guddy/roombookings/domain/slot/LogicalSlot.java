package fr.guddy.roombookings.domain.slot;

import java.util.HashMap;
import java.util.Map;

public final class LogicalSlot implements Slot {
    private final long timestampStart;
    private final long timestampEnd;

    public LogicalSlot(final long timestampStart, final long timestampEnd) {
        this.timestampStart = Long.min(timestampStart, timestampEnd);
        this.timestampEnd = Long.max(timestampStart, timestampEnd);
    }

    @Override
    public long timestampStart() {
        return timestampStart;
    }

    @Override
    public long timestampEnd() {
        return timestampEnd;
    }

    @Override
    public Map<String, Object> map() {
        final Map<String, Object> map = new HashMap<>();
        map.put("timestampStart", timestampStart);
        map.put("timestampEnd", timestampEnd);
        return map;
    }
}
