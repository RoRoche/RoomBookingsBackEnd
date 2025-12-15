/*
 * MIT License
 *
 * Copyright (c) 2018-2025 Romain Rochegude
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package fr.guddy.roombookings.domain.room;

import java.util.Map;
import javax.json.JsonObject;
import org.cactoos.Scalar;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;

public final class JsonRoom extends RoomEnvelope {

  private static final String JSON_KEY_NAME = "name";
  private static final String JSON_KEY_CAPACITY = "capacity";

  public JsonRoom(final Room delegate) {
    super(delegate);
  }

  public JsonRoom(final JsonObject jsonObject) {
    this(new SimpleRoom(jsonObject.getString(JSON_KEY_NAME), jsonObject.getInt(JSON_KEY_CAPACITY)));
  }

  public JsonRoom(final String body) {
    this(new Parsed(body).value());
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

    private Parsed(final String body) {
      this.body = body;
    }

    @Override
    public JsonObject value() {
      try (final var reader = javax.json.Json.createReader(new java.io.StringReader(this.body))) {
        return reader.readObject();
      }
    }
  }
}
