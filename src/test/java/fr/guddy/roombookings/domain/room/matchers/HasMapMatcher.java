package fr.guddy.roombookings.domain.room.matchers;

import fr.guddy.roombookings.assertions.Matcher;
import fr.guddy.roombookings.domain.room.Room;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public final class HasMapMatcher implements Matcher<Room> {

    private final Pair<String, Object>[] expectedEntries;

    @SafeVarargs
    public HasMapMatcher(final Pair<String, Object>... expectedEntries) {
        this.expectedEntries = expectedEntries;
    }

    @Override
    public void check(final Room room) {
        final List<String> keys = Stream.of(expectedEntries)
                .map(Pair::getKey)
                .toList();
        assertThat(room.map().keySet())
                .describedAs("Room map")
                .containsExactlyElementsOf(keys);
        Stream.of(expectedEntries)
                .forEach(pair -> {
                    assertThat(room.map())
                            .describedAs("Room map")
                            .containsEntry(pair.getKey(), pair.getValue());
                });
    }
}
