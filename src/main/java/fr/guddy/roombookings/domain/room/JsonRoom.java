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

import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.cactoos.Scalar;

/**
 * JSON {@link Room}.
 *
 * @since 1.0.0
 */
public final class JsonRoom implements Room {

    /**
     * The JSON key for name.
     */
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private static final String NAME = "name";

    /**
     * The JSON key for capacity.
     */
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private static final String CAPACITY = "capacity";

    /**
     * The wrapped {@link Room}.
     */
    private final Room delegate;

    public JsonRoom(final Room delegate) {
        this.delegate = delegate;
    }

    public JsonRoom(final JsonObject json) {
        this(
            new SimpleRoom(
                json.getString(JsonRoom.NAME),
                json.getInt(JsonRoom.CAPACITY)
            )
        );
    }

    public JsonRoom(final String body) {
        this(new Parsed(body).value());
    }

    @Override
    public String name() {
        return this.delegate.name();
    }

    @Override
    public int capacity() {
        return this.delegate.capacity();
    }

    @Override
    public Map<String, Object> map() {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(JsonRoom.NAME, this.name());
        map.put(JsonRoom.CAPACITY, this.capacity());
        return map;
    }

    /**
     * {@link Scalar} to parse a {@link JsonObject} from JSON body as {@link String}.
     *
     * @since 1.0.0
     */
    private static final class Parsed implements Scalar<JsonObject> {

        /**
         * The JSON body as {@link String}.
         */
        private final String body;

        private Parsed(final String body) {
            this.body = body;
        }

        @SuppressWarnings("PMD.UnnecessaryLocalRule")
        @Override
        public JsonObject value() {
            try (JsonReader reader = Json.createReader(new StringReader(this.body))) {
                return reader.readObject();
            }
        }
    }
}
