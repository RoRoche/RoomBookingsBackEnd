package fr.guddy.roombookings.domain.slot;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

public final class JsonSlot implements Slot {
    private static final String JSON_KEY_TIMESTAMP_START = "timestamp_start";
    private static final String JSON_KEY_TIMESTAMP_END = "timestamp_end";

    private final Slot delegate;

    public JsonSlot(final Slot delegate) {
        this.delegate = delegate;
    }

    public JsonSlot(final String body) {
        this(
                Json.createReader(
                        new StringReader(body)
                ).readObject()
        );
    }

    public JsonSlot(final JsonObject jsonObject) {
        this(
                new LogicalSlot(
                        jsonObject.getInt(JSON_KEY_TIMESTAMP_START),
                        jsonObject.getInt(JSON_KEY_TIMESTAMP_END)
                )
        );
    }

    @Override
    public long timestampStart() {
        return delegate.timestampStart();
    }

    @Override
    public long timestampEnd() {
        return delegate.timestampEnd();
    }

    @Override
    public Map<String, Object> map() {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(JSON_KEY_TIMESTAMP_START, timestampStart());
        map.put(JSON_KEY_TIMESTAMP_END, timestampEnd());
        return map;
    }

}
