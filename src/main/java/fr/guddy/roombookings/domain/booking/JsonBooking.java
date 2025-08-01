package fr.guddy.roombookings.domain.booking;

import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.slot.JsonSlot;

import javax.json.JsonObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class JsonBooking extends Booking.Envelope {
    private static final String JSON_KEY_ID = "id";
    private static final String JSON_KEY_USER_ID = "user_id";
    private static final String JSON_KEY_ROOM = "room";
    private static final String JSON_KEY_SLOT = "slot";

    public JsonBooking(final Booking delegate) {
        super(delegate);
    }

    public JsonBooking(final String body) {
        this(new JsonFromBody(body));
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

    public JsonBooking(final Supplier<JsonObject> supplier) {
        this(supplier.get());
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
