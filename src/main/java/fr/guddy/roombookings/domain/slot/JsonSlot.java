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
package fr.guddy.roombookings.domain.slot;

import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;

public final class JsonSlot implements Slot {

  private static final String JSON_KEY_TIMESTAMP_START = "timestamp_start";
  private static final String JSON_KEY_TIMESTAMP_END = "timestamp_end";

  private final Slot delegate;

  public JsonSlot(final Slot delegate) {
    this.delegate = delegate;
  }

  public JsonSlot(final String body) {
    this(Json.createReader(new StringReader(body)).readObject());
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
