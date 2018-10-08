package fr.guddy.roombookings.infra.routes;

import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.requests.GetRoomsRequest;
import fr.guddy.roombookings.infra.requests.PostRoomRequest;
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
        get(ctx -> new GetRoomsRequest(rooms).perform(ctx));
        get(":name", (ctx) -> {

        });
        post(ctx -> new PostRoomRequest(rooms, ctx).perform(ctx));
    }
}
