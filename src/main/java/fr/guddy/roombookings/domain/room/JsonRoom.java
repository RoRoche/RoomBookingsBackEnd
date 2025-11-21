package fr.guddy.roombookings.domain.room;

import org.cactoos.Scalar;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;

import javax.json.JsonObject;
import java.util.Map;

public final class JsonRoom extends RoomEnvelope {
    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_CAPACITY = "capacity";

    public JsonRoom(final Room delegate) {
        super(delegate);
    }

    public JsonRoom(final JsonObject jsonObject) {
        this(
                new SimpleRoom(
                        jsonObject.getString(JSON_KEY_NAME),
                        jsonObject.getInt(JSON_KEY_CAPACITY)
                )
        );
    }

    public JsonRoom(final String body) {
        this(
                new Parsed(body).value()
        );
    }

    @Override
    public Map<String, Object> map() {
        return new MapOf<String, Object>(
                new MapEntry<>(JsonRoom.JSON_KEY_NAME, name()),
                new MapEntry<>(JsonRoom.JSON_KEY_CAPACITY, capacity())
        );
    }

    private static final class Parsed implements Scalar<JsonObject> {

        private final String body;

        private Parsed(String body) {
            this.body = body;
        }

        @Override
        public JsonObject value() {
            try (var reader = javax.json.Json.createReader(new java.io.StringReader(this.body))) {
                return reader.readObject();
            }
        }
    }
}
