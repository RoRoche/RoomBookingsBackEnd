package fr.guddy.roombookings.domain.slot;

import java.util.Map;

public interface Slot {
    long timestampStart();

    long timestampEnd();

    Map<String, Object> map();
}
