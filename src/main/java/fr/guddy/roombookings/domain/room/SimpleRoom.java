package fr.guddy.roombookings.domain.room;

import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;

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
        return this.name;
    }

    @Override
    public int capacity() {
        return this.capacity;
    }

    @Override
    public Map<String, Object> map() {
        return new MapOf<String, Object>(
                new MapEntry<>("name", this.name),
                new MapEntry<>("capacity", this.capacity)
        );
    }
}
