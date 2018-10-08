package fr.guddy.roombookings.infra.params;

import io.javalin.Context;

public final class PathParameter implements Parameter<String> {

    private final String name;
    private final Parameter<String> delegate;

    public PathParameter(final String name, final Parameter<String> delegate) {
        this.name = name;
        this.delegate = delegate;
    }

    public PathParameter(final String name, final Context context) {
        this(
                name,
                new StringParameter(name, context.pathParam(name))
        );
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String value() {
        return delegate.value();
    }
}
