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

import fr.guddy.roombookings.domain.room.Room;
import fr.guddy.roombookings.domain.slot.Slot;
import java.util.Map;

/**
 * Define how to book a {@link Room}.
 *
 * @since 1.0.0
 */
public interface Booking {
    /**
     * The booking ID.
     *
     * @return The booking ID.
     */
    Long identifier();

    /**
     * The user ID.
     *
     * @return The user ID.
     */
    String userId();

    /**
     * The booked {@link Room}.
     *
     * @return The booked {@link Room}.
     */
    Room room();

    /**
     * The booked {@link Slot}.
     *
     * @return The booked {@link Slot}.
     */
    Slot slot();

    /**
     * The map populated with inner details.
     *
     * @return The map populated with inner details.
     */
    Map<String, Object> map();
}
