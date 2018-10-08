package fr.guddy.roombookings.infra.params;

import java.util.Optional;

public final class OptionalParameter<T> implements Parameter<Optional<T>> {

    private final Parameter<T> delegate;

    public OptionalParameter(final Parameter<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public Optional<T> value() {
        return Optional.ofNullable(delegate.value());
    }
}
