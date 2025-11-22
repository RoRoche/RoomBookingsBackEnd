package fr.guddy.roombookings.domain.room.matchers;

import fr.guddy.roombookings.domain.room.Room;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public final class HasCapacityMatcher extends TypeSafeMatcher<Room> {

  private final int expectedCapacity;

  public HasCapacityMatcher(final int expectedCapacity) {
    this.expectedCapacity = expectedCapacity;
  }

  @Override
  protected boolean matchesSafely(final Room room) {
    return room.capacity() == expectedCapacity;
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("a Room with capacity ").appendValue(expectedCapacity);
  }

  @Override
  protected void describeMismatchSafely(final Room room, final Description mismatchDescription) {
    mismatchDescription.appendText("was a Room with capacity ").appendValue(room.capacity());
  }
}
