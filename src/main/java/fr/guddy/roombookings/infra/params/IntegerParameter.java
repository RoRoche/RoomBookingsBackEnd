package fr.guddy.roombookings.infra.params;

import fr.guddy.roombookings.infra.params.exceptions.NotProcessableParameterException;
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
        final String value = delegate.value();
        return Try.of(() ->
                Integer.valueOf(value)
        ).getOrElseThrow(throwable ->
                new NotProcessableParameterException(name(), Integer.class)
        );
    }
}
