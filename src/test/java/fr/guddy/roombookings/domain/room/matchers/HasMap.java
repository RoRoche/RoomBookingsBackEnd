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
