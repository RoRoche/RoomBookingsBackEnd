package fr.guddy.roombookings.assertions;

import java.util.List;

public final class MatchersAssertion<T> implements Assertion {

    private final T sut;
    private final List<Matcher<T>> matchers;

    public MatchersAssertion(final T sut, final List<Matcher<T>> matchers) {
        this.sut = sut;
        this.matchers = matchers;
    }

    public MatchersAssertion(final T sut, final Matcher<T>... matchers) {
        this(sut, List.of(matchers));
    }

    @Override
    public void check() {
        matchers.forEach(matcher -> matcher.check(sut));
    }
}
