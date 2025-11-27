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
