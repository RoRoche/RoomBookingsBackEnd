package fr.guddy.roombookings.infra.handlers;

import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import io.javalin.Context;
import io.javalin.ExceptionHandler;
import org.eclipse.jetty.http.HttpStatus;

public final class RoomNotFoundHandler implements ExceptionHandler<RoomNotFoundException> {
    @Override
    public void handle(final RoomNotFoundException exception, final Context ctx) {
        ctx.status(HttpStatus.NOT_FOUND_404).result(exception.getMessage());
    }
}
