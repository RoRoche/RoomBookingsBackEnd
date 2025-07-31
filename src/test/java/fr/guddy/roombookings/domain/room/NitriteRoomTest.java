package fr.guddy.roombookings.domain.room;

import fr.guddy.roombookings.assertions.MatchersAssertion;
import fr.guddy.roombookings.domain.room.matchers.HasCapacityMatcher;
import fr.guddy.roombookings.domain.room.matchers.HasMapMatcher;
import fr.guddy.roombookings.domain.room.matchers.HasNameMatcher;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

final class NitriteRoomTest {
    @Test
    void testOk() {
        new MatchersAssertion<>(
                new NitriteRoom(
                        new SimpleRoom("test", 12)
                ),
                new HasNameMatcher("test"),
                new HasCapacityMatcher(12),
                new HasMapMatcher(
                        Pair.of("room_name", "test"),
                        Pair.of("room_capacity", 12)
                )
        ).check();
    }
}