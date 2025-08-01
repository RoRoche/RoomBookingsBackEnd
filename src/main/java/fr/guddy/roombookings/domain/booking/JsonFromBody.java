package fr.guddy.roombookings.domain.booking;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.function.Supplier;

public class JsonFromBody implements Supplier<JsonObject> {
    private final String body;

    public JsonFromBody(final String body) {
        this.body = body;
    }

    @Override
    public JsonObject get() {
        try (final JsonReader reader = Json.createReader(new StringReader(this.body))) {
            return reader.readObject();
        }
    }
}
