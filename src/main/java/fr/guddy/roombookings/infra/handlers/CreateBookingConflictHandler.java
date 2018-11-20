package fr.guddy.roombookings.infra.handlers;

import fr.guddy.roombookings.domain.rooms.CreateRoomConflictException;
import io.javalin.Context;
import io.javalin.ExceptionHandler;
import org.eclipse.jetty.http.HttpStatus;

public final class CreateRoomConflictHandler implements ExceptionHandler<CreateRoomConflictException> {
    @Override
    public void handle(final CreateRoomConflictException exception, final Context ctx) {
        ctx.status(HttpStatus.CONFLICT_409).result(exception.getMessage());
    }
}
