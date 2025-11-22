package fr.guddy.roombookings.infra.routes;

import static io.javalin.apibuilder.ApiBuilder.get;

import io.javalin.apibuilder.EndpointGroup;

public final class ReadinessRoute implements EndpointGroup {

  @Override
  public void addEndpoints() {
    get((ctx) -> ctx.status(200).result("READY"));
  }
}
