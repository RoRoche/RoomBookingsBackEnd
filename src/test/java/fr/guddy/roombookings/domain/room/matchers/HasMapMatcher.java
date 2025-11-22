package fr.guddy.roombookings.domain.room.matchers;

import fr.guddy.roombookings.domain.room.Room;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public final class HasMapMatcher extends TypeSafeMatcher<Room> {

  private final Pair<String, Object>[] expectedEntries;

  @SafeVarargs
  public HasMapMatcher(final Pair<String, Object>... expectedEntries) {
    this.expectedEntries = expectedEntries;
  }

  @Override
  protected boolean matchesSafely(final Room room) {
    final Map<String, Object> roomMap = room.map();

    final Set<String> expectedKeys = Arrays.stream(expectedEntries)
      .map(Pair::getKey)
      .collect(Collectors.toSet());

    if (!roomMap.keySet().equals(expectedKeys)) {
      return false;
    }

    return Arrays.stream(expectedEntries).allMatch(
      (pair) ->
        roomMap.containsKey(pair.getKey()) && roomMap.get(pair.getKey()).equals(pair.getValue())
    );
  }

  @Override
  public void describeTo(final Description description) {
    description
      .appendText("a Room with map entries: ")
      .appendValueList("{", ", ", "}", expectedEntries);
  }

  @Override
  protected void describeMismatchSafely(final Room room, final Description mismatchDescription) {
    mismatchDescription.appendText("was a Room with map entries: ").appendValue(room.map());
  }
}
