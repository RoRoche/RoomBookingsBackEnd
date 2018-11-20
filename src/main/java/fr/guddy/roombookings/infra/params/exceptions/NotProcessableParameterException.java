package fr.guddy.roombookings.infra.params;

public final class NotProcessableException extends RuntimeException {
    public NotProcessableException(final String name, final Class<?> clazz) {
        super(
                String.format(
                        "Parameter '%s' could not be processed, it should be of type %s",
                        name,
                        clazz.getSimpleName()
                )
        );
    }
}
