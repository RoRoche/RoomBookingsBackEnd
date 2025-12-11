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
package fr.guddy.roombookings.domain.room.matchers;

import fr.guddy.roombookings.domain.room.Room;
import java.util.Map;
import org.cactoos.map.MapOf;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public final class HasMap extends TypeSafeMatcher<Room> {

  private final Map<String, Object> expected;

  public HasMap(final Map<String, Object> expected) {
    this.expected = expected;
  }

  @SafeVarargs
  public HasMap(final Map.Entry<String, Object>... expected) {
    this(new MapOf<>(expected));
  }

  @Override
  protected boolean matchesSafely(final Room actual) {
    return this.expected.equals(actual.map());
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("a Room with map entries: ").appendValue(this.expected);
  }

  @Override
  protected void describeMismatchSafely(final Room room, final Description mismatchDescription) {
    mismatchDescription.appendText("was a Room with map entries: ").appendValue(room.map());
  }
}
