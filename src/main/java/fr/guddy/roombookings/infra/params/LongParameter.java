package fr.guddy.roombookings.infra.params;

import io.vavr.control.Try;

public final class LongParameter implements Parameter<Long> {

    private final Parameter<String> delegate;

    public LongParameter(final Parameter<String> delegate) {
        this.delegate = delegate;
    }

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public Long value() {
        return Try.of(() ->
                Long.valueOf(delegate.value())
        ).getOrElseThrow(throwable ->
                new NotProcessableException(name(), Long.class)
        );
    }
}
