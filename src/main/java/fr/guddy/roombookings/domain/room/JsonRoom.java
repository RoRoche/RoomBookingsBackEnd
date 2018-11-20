package fr.guddy.roombookings.domain.room;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

public final class JsonRoom implements Room {
    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_CAPACITY = "capacity";

    private final Room delegate;

    public JsonRoom(final Room delegate) {
        this.delegate = delegate;
    }

    public JsonRoom(final String body) {
        this(
                Json.createReader(
                        new StringReader(body)
                ).readObject()
        );
    }

    public JsonRoom(final JsonObject jsonObject) {
        this(
                new SimpleRoom(
                        jsonObject.getString(JSON_KEY_NAME),
                        jsonObject.getInt(JSON_KEY_CAPACITY)
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
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(JSON_KEY_NAME, name());
        map.put(JSON_KEY_CAPACITY, capacity());
        return map;
    }
}
