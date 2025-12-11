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
package fr.guddy.roombookings.infra.matchers;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public final class DeepMatches implements BiFunction<Object, Object, Boolean> {

  @Override
  public Boolean apply(final Object actual, final Object expected) {
    return switch (expected) {
      case null -> actual == null;
      case Number e when actual instanceof Number a -> a.longValue() == e.longValue();
      case Map<?, ?> expectedMap when actual instanceof Map<?, ?> actualMap -> expectedMap
        .entrySet()
        .stream()
        .allMatch(
          (entry) ->
            actualMap.containsKey(entry.getKey()) &&
            new DeepMatches().apply(actualMap.get(entry.getKey()), entry.getValue())
        );
      case List<?> expectedList when actual instanceof List<?> actualList -> (expectedList.size() ==
          actualList.size() &&
        IntStream.range(0, expectedList.size()).allMatch((i) ->
          new DeepMatches().apply(actualList.get(i), actualList.get(i))
        ));
      default -> Objects.equals(actual, expected);
    };
  }
}
