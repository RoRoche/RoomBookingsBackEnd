package fr.guddy.roombookings.domain.slots;

import org.joda.time.Instant;

public interface Slot {
    Instant start();

    Instant end();
}
