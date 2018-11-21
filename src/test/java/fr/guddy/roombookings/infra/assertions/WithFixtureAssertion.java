package fr.guddy.roombookings.infra.assertions;

import fr.guddy.roombookings.assertions.Assertion;
import fr.guddy.roombookings.domain.fixtures.Fixture;

public final class WithFixtureAssertion implements Assertion {

    private final Fixture fixture;
    private final Assertion assertion;

    public WithFixtureAssertion(final Fixture fixture, final Assertion assertion) {
        this.fixture = fixture;
        this.assertion = assertion;
    }

    @Override
    public void check() {
        fixture.perform();
        assertion.check();
    }
}
