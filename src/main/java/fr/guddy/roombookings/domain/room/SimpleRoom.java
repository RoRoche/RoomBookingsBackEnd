package fr.guddy.roombookings.domain.room;

import java.util.LinkedHashMap;
import java.util.Map;

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
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", name);
        map.put("capacity", capacity);
        return map;
    }
}
