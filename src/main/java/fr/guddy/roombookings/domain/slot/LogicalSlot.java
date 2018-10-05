package fr.guddy.roombookings.domain.slot;

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
}
