package fr.guddy.roombookings.infra.routes;

import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.params.IntegerParameter;
import fr.guddy.roombookings.infra.params.OptionalParameter;
import fr.guddy.roombookings.infra.params.QueryParameter;
import fr.guddy.roombookings.infra.params.StringParameter;
import fr.guddy.roombookings.infra.requests.*;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public final class RoomsRoute implements EndpointGroup {
    private final Rooms rooms;

    public RoomsRoute(final Rooms rooms) {
        this.rooms = rooms;
    }

    @Override
    public void addEndpoints() {
        get(
                ctx ->
                        new OptionalParameter<>(
                                        new QueryParameter("capacity", ctx)
                        )
                                .value()
                                .<Request>map(capacity ->
                                        new GetCapableRoomsRequest(
                                                rooms,
                                                new IntegerParameter(
                                                        new StringParameter(
                                                                "name",
                                                                capacity
                                                        )
                                                )
                                        )
                                ).orElseGet(() ->
                                new GetRoomsRequest(rooms)
                        ).perform(ctx)
        );
        get(
                ":name",
                (ctx) ->
                        new GetNamedRoomRequest(rooms, ctx).perform(ctx)
        );
        post(
                ctx ->
                        new PostRoomRequest(rooms, ctx).perform(ctx)
        );
    }
}
