package fr.guddy.roombookings.infra.params;

public interface Parameter<T> {
    String name();

    T value();
}
