package fr.guddy.roombookings.infra;

import fr.guddy.roombookings.domain.bookings.BookingNotDeletedException;
import fr.guddy.roombookings.domain.bookings.BookingNotFoundException;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.bookings.CreateBookingConflictException;
import fr.guddy.roombookings.domain.rooms.CreateRoomConflictException;
import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.handlers.*;
import fr.guddy.roombookings.infra.params.exceptions.MissingParameterException;
import fr.guddy.roombookings.infra.params.exceptions.NotProcessableParameterException;
import fr.guddy.roombookings.infra.ports.Port;
import fr.guddy.roombookings.infra.routes.BookingsRoute;
import fr.guddy.roombookings.infra.routes.RoomsRoute;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.path;

public final class Api {
    private final Javalin app;
    private final Port port;

    public Api(final Javalin app, final Port port) {
        this.app = app;
        this.port = port;
    }

    public Api(final Port port, final Rooms rooms, final Bookings bookings) {
        this(
                Javalin.create()
                        .routes(() -> {
                            path("rooms", new RoomsRoute(rooms, bookings));
                            path("bookings", new BookingsRoute(rooms, bookings));
                        })
                        .exception(NotProcessableParameterException.class, new NotProcessableParameterHandler())
                        .exception(MissingParameterException.class, new MissingParameterHandler())
                        .exception(RoomNotFoundException.class, new RoomNotFoundHandler())
                        .exception(BookingNotFoundException.class, new BookingNotFoundHandler())
                        .exception(BookingNotDeletedException.class, new BookingNotDeletedHandler())
                        .exception(CreateRoomConflictException.class, new CreateRoomConflictHandler())
                        .exception(CreateBookingConflictException.class, new CreateBookingConflictHandler()),
                port
        );
    }

    public void start() {
        app.start(port.value());
    }

    public void stop() {
        app.stop();
    }
}
