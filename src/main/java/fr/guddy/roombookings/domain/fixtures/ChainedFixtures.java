package fr.guddy.roombookings.domain.fixtures;

import org.dizitart.no2.WriteResult;

import java.util.List;

public final class ChainedFixtures implements Fixture {
    private final List<Fixture> fixtures;

    public ChainedFixtures(final List<Fixture> fixtures) {
        this.fixtures = fixtures;
    }

    public ChainedFixtures(final Fixture... fixtures) {
        this(
                List.of(fixtures)
        );
    }

    @Override
    public WriteResult perform() {
        fixtures.forEach(Fixture::perform);
        return null;
    }
}
