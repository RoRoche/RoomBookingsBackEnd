package fr.guddy.roombookings.infra.params;

import fr.guddy.roombookings.infra.params.exceptions.NotProcessableParameterException;
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
        final String value = delegate.value();
        return Try.of(() ->
                Long.valueOf(value)
        ).getOrElseThrow(throwable ->
                new NotProcessableParameterException(name(), Long.class)
        );
    }
}
