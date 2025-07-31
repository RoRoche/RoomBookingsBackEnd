package fr.guddy.roombookings.domain.booking;

import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.slot.JsonSlot;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class JsonBooking extends Booking.Envelope {
    private static final String JSON_KEY_ID = "id";
    private static final String JSON_KEY_USER_ID = "user_id";
    private static final String JSON_KEY_ROOM = "room";
    private static final String JSON_KEY_SLOT = "slot";

    private static JsonObject readJsonObject(String body) {
        try (final JsonReader reader = Json.createReader(new StringReader(body))) {
            return reader.readObject();
        }
    }

    public JsonBooking(final Booking delegate) {
        super(delegate);
    }

    public JsonBooking(final String body) {
        this(readJsonObject(body));
    }

    public JsonBooking(final JsonObject jsonObject) {
        this(
                new SimpleBooking(
                        (long) jsonObject.getInt(JSON_KEY_ID, -1),
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
