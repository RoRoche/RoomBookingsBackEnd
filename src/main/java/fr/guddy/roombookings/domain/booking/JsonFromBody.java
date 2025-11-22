package fr.guddy.roombookings.domain.booking;

import org.cactoos.Scalar;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public final class JsonFromBody implements Scalar<JsonObject> {
  private final String body;

  public JsonFromBody(final String body) {
    this.body = body;
  }

  @Override
  public JsonObject value() {
    try (final JsonReader reader = Json.createReader(new StringReader(this.body))) {
      return reader.readObject();
    }
  }
}
