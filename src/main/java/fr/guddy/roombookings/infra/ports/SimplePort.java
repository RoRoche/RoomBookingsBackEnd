package fr.guddy.roombookings.infra.ports;

public final class SimplePort implements Port {

  private final int port;

  public SimplePort(int port) {
    this.port = port;
  }

  @Override
  public int value() {
    return this.port;
  }
}
