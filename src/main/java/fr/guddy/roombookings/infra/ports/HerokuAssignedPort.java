package fr.guddy.roombookings.infra.ports;

import java.util.Optional;

public final class HerokuAssignedPort implements Port {

    private static final String PORT = "PORT";

    private final Port defaultPort;

    public HerokuAssignedPort(final Port defaultPort) {
        this.defaultPort = defaultPort;
    }

    @Override
    public int value() {
        final ProcessBuilder processBuilder = new ProcessBuilder();
        final String envVar = processBuilder.environment().get(PORT);
        return Optional.ofNullable(envVar)
                .map(Integer::parseInt)
                .orElseGet(defaultPort::value);
    }
}
