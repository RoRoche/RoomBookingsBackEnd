package fr.guddy.roombookings.infra.routes;

import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.infra.requests.DeleteBookingRequest;
import fr.guddy.roombookings.infra.requests.GetRemindersRequest;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;

public final class BookingsRoute implements EndpointGroup {
    private final Bookings bookings;

    public BookingsRoute(final Bookings bookings) {
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
                        new DeleteBookingRequest(bookings, ctx).perform(ctx)
        );
    }
}
