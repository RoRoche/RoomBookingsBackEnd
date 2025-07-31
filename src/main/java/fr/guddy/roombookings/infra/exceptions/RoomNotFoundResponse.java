package fr.guddy.roombookings.infra.exceptions;

import fr.guddy.roombookings.domain.rooms.RoomNotFoundException;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.eclipse.jetty.http.HttpStatus;

public final class RoomNotFoundResponse implements ExceptionHandler<RoomNotFoundException> {
    @Override
    public void handle(final RoomNotFoundException exception, final Context ctx) {
        ctx.status(HttpStatus.NOT_FOUND_404).result(exception.getMessage());
    }
}
