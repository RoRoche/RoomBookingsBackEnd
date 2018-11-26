package fr.guddy.roombookings.assertions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MatchersAssertion<T> implements Assertion {

    private final T sut;
    private final List<Matcher<T>> matchers;

    public MatchersAssertion(final T sut, final List<Matcher<T>> matchers) {
        this.sut = sut;
        this.matchers = matchers;
    }

    public MatchersAssertion(final T sut, final Matcher<T>... matchers) {
        this(sut, Stream.of(matchers).collect(Collectors.toList()));
    }

    @Override
    public void check() {
        matchers.forEach(matcher -> matcher.check(sut));
    }
}
