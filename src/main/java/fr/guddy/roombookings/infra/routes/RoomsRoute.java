package fr.guddy.roombookings.infra.routes;

import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.params.IntegerParameter;
import fr.guddy.roombookings.infra.params.OptionalParameter;
import fr.guddy.roombookings.infra.params.QueryParameter;
import fr.guddy.roombookings.infra.params.StringParameter;
import fr.guddy.roombookings.infra.requests.*;
import io.javalin.apibuilder.EndpointGroup;

import java.util.stream.Stream;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public final class RoomsRoute implements EndpointGroup {
    private final Rooms rooms;
    private final Bookings bookings;

    public RoomsRoute(final Rooms rooms, final Bookings bookings) {
        this.rooms = rooms;
        this.bookings = bookings;
    }

    @Override
    public void addEndpoints() {
        get(
                ctx -> {
                    final OptionalParameter<String> capacityParameter = new OptionalParameter<>(
                            new QueryParameter("capacity", ctx)
                    );
                    final OptionalParameter<String> timestampStartParameter = new OptionalParameter<>(
                            new QueryParameter("timestamp_start", ctx)
                    );
                    final Request request;
                    if (Stream.of(capacityParameter, timestampStartParameter)
                            .allMatch(parameter -> parameter.value().isPresent())) {
                        request = new GetAvailableRoomsRequest(rooms, bookings, ctx);
                    } else {
                        request = capacityParameter
                                .value()
                                .<Request>map(capacity ->
                                        new GetCapableRoomsRequest(
                                                rooms,
                                                new IntegerParameter(
                                                        new StringParameter(
                                                                "capacity",
                                                                capacity
                                                        )
                                                )
                                        )
                                ).orElseGet(() ->
                                        new GetRoomsRequest(rooms)
                                );
                    }
                    request.perform(ctx);
                }
        );
        get(
                "/:name",
                ctx ->
                        new GetNamedRoomRequest(rooms, ctx).perform(ctx)
        );
        get(
                "/:name/bookings",
                ctx ->
                        new GetBookingsForRoomInSlotRequest(rooms, bookings, ctx).perform(ctx)
        );
        post(
                "/:name/bookings",
                ctx ->
                        new PostBookingRequest(rooms, bookings, ctx).perform(ctx)
        );
        post(
                ctx ->
                        new PostRoomRequest(rooms, ctx).perform(ctx)
        );
    }
}
