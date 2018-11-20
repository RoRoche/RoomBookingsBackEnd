package fr.guddy.roombookings.domain.room.matchers;

import fr.guddy.roombookings.assertions.Matcher;
import fr.guddy.roombookings.domain.room.Room;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public final class HasMapMatcher implements Matcher<Room> {

    private Map.Entry<? extends String, ?>[] expectedEntries;

    @SafeVarargs
    public HasMapMatcher(final Map.Entry<? extends String, ?>... expectedEntries) {
        this.expectedEntries = expectedEntries;
    }

    @Override
    public void check(final Room room) {
        assertThat(room.map()).describedAs("Room map").containsExactly(expectedEntries);
    }
}
