package fr.guddy.roombookings.infra.params;

import io.javalin.Context;

public final class PathParameter implements Parameter<String> {

    private final Parameter<String> delegate;

    public PathParameter(final Parameter<String> delegate) {
        this.delegate = delegate;
    }

    public PathParameter(final Context context, final String name) {
        this(
                new StringParameter(context.pathParam(name))
        );
    }

    @Override
    public String value() {
        return delegate.value();
    }
}
