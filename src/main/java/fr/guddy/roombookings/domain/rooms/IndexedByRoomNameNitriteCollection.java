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
package fr.guddy.roombookings.domain.rooms;

import org.cactoos.Scalar;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.NitriteCollection;

/**
 * A {@link NitriteCollection} with index on {@link fr.guddy.roombookings.domain.room.Room} name.
 *
 * @since 1.0.0
 */
public final class IndexedByRoomNameNitriteCollection implements Scalar<NitriteCollection> {

    /**
     * The index on {@link fr.guddy.roombookings.domain.room.Room} name.
     */
    private static final String INDEX_ROOM_NAME = "room_name";

    /**
     * The concrete {@link Nitrite} database on which to apply the index.
     */
    private final Nitrite database;

    public IndexedByRoomNameNitriteCollection(final Nitrite database) {
        this.database = database;
    }

    @Override
    public NitriteCollection value() {
        final NitriteCollection rooms = this.database.getCollection("rooms");
        if (!rooms.hasIndex(IndexedByRoomNameNitriteCollection.INDEX_ROOM_NAME)) {
            rooms.createIndex(IndexedByRoomNameNitriteCollection.INDEX_ROOM_NAME);
        }
        return rooms;
    }
}
