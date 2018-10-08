package fr.guddy.roombookings.infra.params;

import io.javalin.Context;

public final class QueryParameter implements Parameter<String> {

    private final Parameter<String> delegate;

    public QueryParameter(final Parameter<String> delegate) {
        this.delegate = delegate;
    }

    public QueryParameter(final Context context, final String name) {
        this(
                new StringParameter(context.queryParam(name))
        );
    }

    @Override
    public String value() {
        return delegate.value();
    }
}
