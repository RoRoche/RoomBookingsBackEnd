package fr.guddy.roombookings.infra.params;

public final class MissingParameterException extends RuntimeException {
    public MissingParameterException(final String name) {
        super(String.format("Parameter named '%s' is missing", name));
    }
}
