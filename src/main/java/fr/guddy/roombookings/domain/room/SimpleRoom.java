package fr.guddy.roombookings.domain.rooms;

import java.util.Map;

import static java.util.Map.entry;

public final class SimpleRoom implements Room {
    private final String name;
    private final int capacity;

    public SimpleRoom(final String name, final int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public Map<String, Object> map() {
        return Map.ofEntries(
                entry("name", name),
                entry("capacity", capacity)
        );
    }
}
