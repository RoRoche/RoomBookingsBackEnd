package fr.guddy.roombookings;

import fr.guddy.roombookings.infra.Api;
import fr.guddy.roombookings.infra.Application;

public final class Main {
  public static void main(final String[] args) {
    final Application api = new Api();
    Runtime.getRuntime().addShutdownHook(new Thread(api::stop));
    api.start();
  }
}
