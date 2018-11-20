package fr.guddy.roombookings.domain.room.matchers;

import fr.guddy.roombookings.assertions.Matcher;
import fr.guddy.roombookings.domain.room.Room;

import static org.assertj.core.api.Assertions.assertThat;

public final class HasNameMatcher implements Matcher<Room> {
    private final String expectedName;

    public HasNameMatcher(final String expectedName) {
        this.expectedName = expectedName;
    }

    @Override
    public void check(final Room room) {
        assertThat(room.name()).describedAs("Room name").isEqualToIgnoringCase(expectedName);
    }
}
