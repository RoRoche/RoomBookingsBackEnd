package fr.guddy.roombookings.infra.params;

import io.javalin.Context;

public final class QueryParameter implements Parameter<String> {

    private final Parameter<String> delegate;

    public QueryParameter(final Parameter<String> delegate) {
        this.delegate = delegate;
    }

    public QueryParameter(final String name, final Context context) {
        this(
                new StringParameter(name, context.queryParam(name))
        );
    }

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public String value() {
        return delegate.value();
    }
}
