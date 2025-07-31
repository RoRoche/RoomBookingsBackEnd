package fr.guddy.roombookings.infra.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;

public class ReadinessRoute implements EndpointGroup {
    @Override
    public void addEndpoints() {
        get(ctx ->
                ctx.status(200).result("READY")
        );
    }
}
