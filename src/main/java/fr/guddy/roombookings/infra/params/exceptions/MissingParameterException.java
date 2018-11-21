package fr.guddy.roombookings.infra.params.exceptions;

public final class MissingParameterException extends RuntimeException {
    public MissingParameterException(final String name) {
        super(String.format("Parameter named '%s' is missing", name));
    }
}
