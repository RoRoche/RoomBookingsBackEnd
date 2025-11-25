package fr.guddy.roombookings.domain.room.matchers;

import fr.guddy.roombookings.domain.room.Room;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public final class HasName extends TypeSafeMatcher<Room> {

  private final String expectedName;

  public HasName(final String expectedName) {
    this.expectedName = expectedName;
  }

  @Override
  protected boolean matchesSafely(final Room room) {
    return room.name().equalsIgnoreCase(expectedName);
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("a Room with name (ignoring case) ").appendValue(expectedName);
  }

  @Override
  protected void describeMismatchSafely(final Room room, final Description mismatchDescription) {
    mismatchDescription.appendText("was a Room with name ").appendValue(room.name());
  }
}
