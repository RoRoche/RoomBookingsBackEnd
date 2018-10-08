package fr.guddy.roombookings.infra.params;

import io.vavr.control.Try;

public final class IntegerParameter implements Parameter<Integer> {

    private final Parameter<String> delegate;

    public IntegerParameter(final Parameter<String> delegate) {
        this.delegate = delegate;
    }

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public Integer value() {
        return Try.of(() ->
                Integer.valueOf(delegate.value())
        ).getOrElseThrow(throwable ->
                new NotProcessableException(name(), Integer.class)
        );
    }
}
