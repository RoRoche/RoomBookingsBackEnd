package fr.guddy.roombookings.domain.room.matchers;

import fr.guddy.roombookings.assertions.Matcher;
import fr.guddy.roombookings.domain.room.Room;

import static org.assertj.core.api.Assertions.assertThat;

public final class HasCapacityMatcher implements Matcher<Room> {
    private final int expectedCapacity;

    public HasCapacityMatcher(final int expectedCapacity) {
        this.expectedCapacity = expectedCapacity;
    }

    @Override
    public void check(final Room room) {
        assertThat(room.capacity()).describedAs("Room capacity").isEqualTo(expectedCapacity);
    }
}
