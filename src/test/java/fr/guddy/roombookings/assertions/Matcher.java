package fr.guddy.roombookings.assertions;

public interface Matcher<T> {
    void check(final T sut);
}
