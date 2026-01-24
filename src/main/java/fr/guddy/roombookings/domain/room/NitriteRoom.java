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

import java.util.LinkedHashMap;
import java.util.Map;
import org.dizitart.no2.collection.Document;

/**
 * {@link Room} stored in {@link org.dizitart.no2.Nitrite}.
 *
 * @since 1.0.0
 */
public final class NitriteRoom implements Room {

    /**
     * The document key for name.
     */
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private static final String NAME = "room_name";

    /**
     * The document key for capacity.
     */
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private static final String CAPACITY = "room_capacity";

    /**
     * The wrapped {@link Room}.
     */
    private final Room delegate;

    public NitriteRoom(final Room delegate) {
        this.delegate = delegate;
    }

    public NitriteRoom(final Document document) {
        this(
            new SimpleRoom(
                document.get(NitriteRoom.NAME, String.class),
                document.get(NitriteRoom.CAPACITY, Integer.class)
            )
        );
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
        map.put(NitriteRoom.NAME, this.name());
        map.put(NitriteRoom.CAPACITY, this.capacity());
        return map;
    }
}
