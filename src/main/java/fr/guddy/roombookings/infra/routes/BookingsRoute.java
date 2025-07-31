package fr.guddy.roombookings.infra.routes;

import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.requests.DeleteBookingRequest;
import fr.guddy.roombookings.infra.requests.GetRemindersRequest;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;

public final class BookingsRoute implements EndpointGroup {
    private final Rooms rooms;
    private final Bookings bookings;

    public BookingsRoute(final Rooms rooms, final Bookings bookings) {
        this.rooms = rooms;
        this.bookings = bookings;
    }

    @Override
    public void addEndpoints() {
        get(
                ctx ->
                        new GetRemindersRequest(bookings, ctx).perform(ctx)
        );
        delete(
                "/{id}",
                ctx ->
                        new DeleteBookingRequest(rooms, bookings, ctx).perform(ctx)
        );
    }
}
