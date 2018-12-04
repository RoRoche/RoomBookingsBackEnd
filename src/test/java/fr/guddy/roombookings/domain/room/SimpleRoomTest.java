package fr.guddy.roombookings.domain.room;

import fr.guddy.roombookings.assertions.MatchersAssertion;
import fr.guddy.roombookings.domain.room.matchers.HasCapacityMatcher;
import fr.guddy.roombookings.domain.room.matchers.HasMapMatcher;
import fr.guddy.roombookings.domain.room.matchers.HasNameMatcher;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

public final class SimpleRoomTest {
    @Test
    public void testOk() {
        new MatchersAssertion<>(
                new SimpleRoom("test", 12),
                new HasNameMatcher("test"),
                new HasCapacityMatcher(12),
                new HasMapMatcher(
                        Pair.of("name", "test"),
                        Pair.of("capacity", 12)
                )
        ).check();
    }
}