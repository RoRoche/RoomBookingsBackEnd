package fr.guddy.roombookings.infra.params;

import fr.guddy.roombookings.infra.params.exceptions.MissingParameterException;

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
        final T value = delegate.value();
        return Optional.ofNullable(value).orElseThrow(() ->
                new MissingParameterException(name())
        );
    }
}
