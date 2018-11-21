package fr.guddy.roombookings.infra.params.exceptions;

public final class NotProcessableParameterException extends RuntimeException {
    public NotProcessableParameterException(final String name, final Class<?> clazz) {
        super(
                String.format(
                        "Parameter '%s' could not be processed, it should be of type %s",
                        name,
                        clazz.getSimpleName()
                )
        );
    }
}
