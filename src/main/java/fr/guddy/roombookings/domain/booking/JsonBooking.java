package fr.guddy.roombookings.domain.booking;

import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.slot.JsonSlot;
import fr.guddy.roombookings.domain.slot.Slot;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class JsonBooking implements Booking {
    private static final String JSON_KEY_ID = "id";
    private static final String JSON_KEY_USER_ID = "user_id";
    private static final String JSON_KEY_ROOM = "room";
    private static final String JSON_KEY_SLOT = "slot";

    private final Booking delegate;

    public JsonBooking(final Booking delegate) {
        this.delegate = delegate;
    }

    public JsonBooking(final String body) {
        this(
                Json.createReader(
                        new StringReader(body)
                ).readObject()
        );
    }

    public JsonBooking(final JsonObject jsonObject) {
        this(
                new SimpleBooking(
                        Integer.valueOf(jsonObject.getInt(JSON_KEY_ID, -1)).longValue(),
                        jsonObject.getString(JSON_KEY_USER_ID),
                        Optional.ofNullable(
                                jsonObject.getJsonObject(JSON_KEY_ROOM)
                        ).map(JsonRoom::new).orElse(null),
                        Optional.ofNullable(
                                jsonObject.getJsonObject(JSON_KEY_SLOT)
                        ).map(JsonSlot::new).orElse(null)
                )
        );
    }

    @Override
    public Long id() {
        return delegate.id();
    }

    @Override
    public String userId() {
        return delegate.userId();
    }

    @Override
    public Room room() {
        return delegate.room();
    }

    @Override
    public Slot slot() {
        return delegate.slot();
    }

    @Override
    public Map<String, Object> map() {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(JSON_KEY_ID, id());
        map.put(JSON_KEY_USER_ID, userId());
        map.put(
                JSON_KEY_ROOM,
                new JsonRoom(room()).map()
        );
        map.put(
                JSON_KEY_SLOT,
                new JsonSlot(slot()).map()
        );
        return map;
    }
}
