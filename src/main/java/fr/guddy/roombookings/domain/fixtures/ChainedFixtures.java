package fr.guddy.roombookings.domain.fixtures;

import java.util.List;
import java.util.stream.Stream;

public final class ChainedFixtures implements Fixture {
    private final List<Fixture> fixtures;

    public ChainedFixtures(final List<Fixture> fixtures) {
        this.fixtures = fixtures;
    }

    public ChainedFixtures(final Fixture... fixtures) {
        this(Stream.of(fixtures).toList());
    }

    @Override
    public Long perform() {
        fixtures.forEach(Fixture::perform);
        return null;
    }
}
