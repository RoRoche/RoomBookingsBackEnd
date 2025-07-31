package fr.guddy.roombookings.infra.params;

public record StringParameter(String name, String value) implements Parameter<String> {
}
