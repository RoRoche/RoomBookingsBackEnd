package fr.guddy.roombookings.infra.params;

public final class StringParameter implements Parameter<String> {
    private final String value;

    public StringParameter(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
