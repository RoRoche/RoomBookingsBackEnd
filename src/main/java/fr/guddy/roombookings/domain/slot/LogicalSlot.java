package fr.guddy.roombookings.domain.slot;

import org.joda.time.Instant;

public final class LogicalSlot implements Slot {
    private static final long MILLIS_FACTOR = 1000L;

    private final Instant start;
    private final Instant end;

    public LogicalSlot(final Instant start, final Instant end) {
        this.start = start;
        this.end = end;
    }

    public LogicalSlot(final long timestampStart, final long timestampEnd) {
        this(
                new Instant(Long.min(timestampStart * MILLIS_FACTOR, timestampEnd * MILLIS_FACTOR)),
                new Instant(Long.max(timestampStart * MILLIS_FACTOR, timestampEnd * MILLIS_FACTOR))
        );
    }

    @Override
    public Instant start() {
        return start;
    }

    @Override
    public Instant end() {
        return end;
    }
}
