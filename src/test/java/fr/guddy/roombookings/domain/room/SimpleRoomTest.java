package fr.guddy.roombookings.domain.room;

import fr.guddy.roombookings.assertions.MatchersAssertion;
import fr.guddy.roombookings.domain.room.matchers.HasCapacityMatcher;
import fr.guddy.roombookings.domain.room.matchers.HasMapMatcher;
import fr.guddy.roombookings.domain.room.matchers.HasNameMatcher;
import org.junit.Test;

import static java.util.Map.entry;

public final class SimpleRoomTest {
    @Test
    public void testOk() {
        new MatchersAssertion<>(
                new SimpleRoom("test", 12),
                new HasNameMatcher("test"),
                new HasCapacityMatcher(12),
                new HasMapMatcher(
                        entry("name", "test"),
                        entry("capacity", 12)
                )
        ).check();
    }
}