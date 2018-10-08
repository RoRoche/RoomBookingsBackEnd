package fr.guddy.roombookings.infra.params;

public final class StringParameter implements Parameter<String> {
    private final String name;
    private final String value;

    public StringParameter(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String value() {
        return value;
    }
}
