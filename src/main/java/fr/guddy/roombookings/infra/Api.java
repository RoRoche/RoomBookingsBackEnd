package fr.guddy.roombookings.infra;

import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.handlers.RoomNotFoundHandler;
import fr.guddy.roombookings.infra.routes.BookingsRoute;
import fr.guddy.roombookings.infra.routes.RoomsRoute;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.path;

public final class Api {
    private final Javalin app;
    private final int port;
    private final Rooms rooms;

    public Api(final Javalin app, final int port, final Rooms rooms) {
        this.app = app;
        this.port = port;
        this.rooms = rooms;
    }

    public Api(final int port, final Rooms rooms) {
        this(
                Javalin.create()
                        .routes(() -> {
                            path("rooms", new RoomsRoute(rooms));
                            path("bookings", new BookingsRoute());
                        })
                        .enableRouteOverview("rooms")
                        .exception(RoomNotFoundException.class, new RoomNotFoundHandler()),
                port,
                rooms);
    }

    public void start() {
        app.start(port);
    }

    public void stop() {
        app.stop();
    }
}