package fr.guddy.roombookings.domain.slot;

import org.joda.time.Instant;

public interface Slot {
    long timestampStart();

    long timestampEnd();
}
