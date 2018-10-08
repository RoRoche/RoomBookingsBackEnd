package fr.guddy.roombookings.infra;

import fr.guddy.roombookings.domain.rooms.Rooms;
import org.junit.rules.ExternalResource;

public final class ApiExternalResource extends ExternalResource {

    private final Api api;

    private ApiExternalResource(final Api api) {
        this.api = api;
    }

    public ApiExternalResource(final int port, final Rooms rooms) {
        this(new Api(port, rooms));
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        api.start();
    }

    @Override
    protected void after() {
        super.after();
        api.stop();
    }
}
