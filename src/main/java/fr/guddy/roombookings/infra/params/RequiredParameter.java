package fr.guddy.roombookings.infra.params;

import java.util.Optional;

public final class RequiredParameter<T> implements Parameter<T> {

    private final Parameter<T> delegate;

    public RequiredParameter(final Parameter<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public T value() {
        return Optional.ofNullable(
                delegate.value()
        ).orElseThrow(() ->
                new MissingParameterException(name())
        );
    }
}
