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
package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.domain.room.JsonRoom;
import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.params.Parameter;
import io.javalin.http.Context;
import java.util.List;
import org.eclipse.jetty.http.HttpStatus;

/**
 * {@link Request} to get {@link Rooms} matching to at least the capacity provied.
 *
 * @since 1.0.0
 */
public final class GetCapableRoomsRequest implements Request {

    /**
     * The collection of {@link Room}.
     */
    private final Rooms rooms;

    /**
     * The targeted capacity.
     */
    private final int capacity;

    public GetCapableRoomsRequest(final Rooms rooms, final int capacity) {
        this.rooms = rooms;
        this.capacity = capacity;
    }

    public GetCapableRoomsRequest(final Rooms rooms, final Parameter<Integer> capacity) {
        this(rooms, capacity.value());
    }

    @Override
    public void perform(final Context context) {
        final List<Room> capable = this.rooms.withCapacity(this.capacity);
        if (capable.isEmpty()) {
            context.status(HttpStatus.NO_CONTENT_204);
        } else {
            context
                .json(capable.stream().map(JsonRoom::new).map(JsonRoom::map).toList())
                .status(HttpStatus.OK_200);
        }
    }
}
