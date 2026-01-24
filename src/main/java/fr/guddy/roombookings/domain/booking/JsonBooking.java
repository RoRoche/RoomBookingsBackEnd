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
package fr.guddy.roombookings.domain.booking;

import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.slot.JsonSlot;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import javax.json.JsonObject;
import org.cactoos.Scalar;
import org.cactoos.scalar.Unchecked;

/**
 * {@link Booking} implementation to deal with JSON content.
 *
 * @since 1.0.0
 */
public final class JsonBooking extends BookingEnvelope {

    /**
     * The JSON key for id.
     */
    private static final String IDENTIFIER = "id";

    /**
     * The JSON key for user's id.
     */
    private static final String USER_ID = "user_id";

    /**
     * The JSON key for room.
     */
    private static final String ROOM = "room";

    /**
     * The JSON key for slot.
     */
    private static final String SLOT = "slot";

    public JsonBooking(final Booking delegate) {
        super(delegate);
    }

    public JsonBooking(final String body) {
        this(new JsonFromBody(body));
    }

    public JsonBooking(final JsonObject json) {
        this(
            new SimpleBooking(
                (long) json.getInt(JsonBooking.IDENTIFIER, -1),
                json.getString(JsonBooking.USER_ID),
                Optional.ofNullable(json.getJsonObject(JsonBooking.ROOM))
                    .map(JsonRoom::new)
                    .orElse(null),
                Optional.ofNullable(json.getJsonObject(JsonBooking.SLOT))
                    .map(JsonSlot::new)
                    .orElse(null)
            )
        );
    }

    public JsonBooking(final Scalar<JsonObject> json) {
        this(new Unchecked<>(json).value());
    }

    @Override
    public Map<String, Object> map() {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(JsonBooking.IDENTIFIER, this.identifier());
        map.put(JsonBooking.USER_ID, this.userId());
        map.put(JsonBooking.ROOM, new JsonRoom(this.room()).map());
        map.put(JsonBooking.SLOT, new JsonSlot(this.slot()).map());
        return map;
    }
}
