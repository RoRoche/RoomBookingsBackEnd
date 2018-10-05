package fr.guddy.roombookings.domain.room;

import org.dizitart.no2.Document;

import java.util.Map;

import static java.util.Map.entry;

public final class NitriteRoom implements Room {
    private static final String DOCUMENT_KEY_NAME = "room_name";
    private static final String DOCUMENT_KEY_CAPACITY = "room_capacity";

    private final Room delegate;

    public NitriteRoom(final Room delegate) {
        this.delegate = delegate;
    }

    public NitriteRoom(final Document document) {
        this(
                new SimpleRoom(
                        document.get(DOCUMENT_KEY_NAME, String.class),
                        document.get(DOCUMENT_KEY_CAPACITY, Integer.class)
                )
        );
    }

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public int capacity() {
        return delegate.capacity();
    }

    @Override
    public Map<String, Object> map() {
        return Map.ofEntries(
                entry(DOCUMENT_KEY_NAME, name()),
                entry(DOCUMENT_KEY_CAPACITY, capacity())
        );
    }
}
